package com.collab.project.service.impl;

import com.collab.project.helpers.Constants;
import com.collab.project.model.art.ArtInfo;
import com.collab.project.model.artist.ArtSample;
import com.collab.project.model.artist.Artist;
import com.collab.project.repositories.ArtistRepository;
import com.collab.project.repositories.ArtistSampleRepository;
import com.collab.project.service.ArtistSampleService;
import com.collab.project.util.FileUtils;
import com.collab.project.util.S3Utils;
import com.collab.project.util.Utils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class ArtistSampleServiceImpl implements ArtistSampleService {

    String bucketName = "artist-samples";

     @Autowired
     S3Utils s3Utils;

    @Autowired
    ArtistSampleRepository artistSampleRepository;

    @Autowired
    ArtistRepository artistRepository;

    @Async
    @Override
    public ArtInfo uploadFile(String artistId, String caption, String fileType, MultipartFile fileToUpload) throws IOException, NoSuchAlgorithmException {
         ArtSample artSample = null;
         String fileExtension = FilenameUtils.getExtension(fileToUpload.getOriginalFilename());
         switch (fileType) {
             case Constants.AUDIO:
                 if (!Constants.ALLOWED_AUDIO_FORMAT.contains(fileExtension)) {
                     throw new IllegalStateException("Invalid file extension");
                 }
                 artSample = uploadAudio(artistId, caption, fileToUpload, fileExtension);
                 break;
             case Constants.VIDEO:
                 if (!Constants.ALLOWED_VIDEO_FORMAT.contains(fileExtension)) {
                     throw new IllegalStateException("Invalid file extension");
                 }
                 artSample = uploadVideo(artistId, caption, fileToUpload, fileExtension);
                 break;
             case Constants.IMAGE:
                 if (!Constants.ALLOWED_IMAGE_FORMAT.contains(fileExtension)) {
                     throw new IllegalStateException("Invalid file extension");
                 }
                 artSample = uploadImage(artistId, caption, fileToUpload, fileExtension);
                 break;
             default:
                 throw new IllegalStateException("Invalid file type for upload");
         }

         ArtInfo artInfo = new ArtInfo(
                 artSample.getCaption(),
                 artSample.getFileType(),
                 artSample.getOriginalUrl(),
                 artSample.getThumbnailUrl(),
                 artSample.getCreatedAt()
         );
        return artInfo;
    }

    private ArtSample uploadImage(String artistId, String caption, MultipartFile fileToUpload, String fileExtension) throws NoSuchAlgorithmException, IOException {
        // Creating original file
         String fileName = Utils.getSHA256(artistId).substring(0, 15) + "_" + System.currentTimeMillis();
         File file = FileUtils.convertMultiPartFileToFile(fileToUpload, fileName + "." + fileExtension);

         // Creating thumbnail file
         FileUtils.createThumbnail(fileToUpload, fileName + "_thumb." + fileExtension);
         File thumbFile = new File(fileName + "_thumb." + fileExtension);

         String originalURL = s3Utils.uploadFileToS3Bucket(bucketName, file, artistId + "/originals", fileName + "." + fileExtension);
         String thumbnailURL = s3Utils.uploadFileToS3Bucket(bucketName, thumbFile, artistId + "/thumbnails", fileName + "." + fileExtension);

         thumbFile.delete();
         file.delete();
         ArtSample artSample = new ArtSample(
                 Constants.FALLBACK_ID,
                 artistId, originalURL,
                 thumbnailURL, caption, Constants.IMAGE,
                 new Timestamp(System.currentTimeMillis()));
         artistSampleRepository.save(artSample);
        return artSample;
    }

    private ArtSample uploadAudio(String artistId, String caption, MultipartFile fileToUpload, String fileExtension) throws NoSuchAlgorithmException, IOException {
         String fileName = Utils.getSHA256(artistId).substring(0, 15) + "_" + System.currentTimeMillis();
         File file = FileUtils.convertMultiPartFileToFile(fileToUpload, fileName + "." + fileExtension);

         String fileURL = s3Utils.uploadFileToS3Bucket(bucketName, file, artistId + "/originals", fileName + "." + fileExtension);
         file.delete();

         ArtSample artSample = new ArtSample(
                 Constants.FALLBACK_ID,
                 artistId, fileURL,
                 fileURL, caption, Constants.AUDIO,
                 new Timestamp(System.currentTimeMillis()));
         artistSampleRepository.save(artSample);
        return artSample;
    }

    private ArtSample uploadVideo(String artistId, String caption, MultipartFile fileToUpload, String fileExtension) throws NoSuchAlgorithmException, IOException {
         String fileName = Utils.getSHA256(artistId).substring(0, 15) + "_" + System.currentTimeMillis();
         File file = FileUtils.convertMultiPartFileToFile(fileToUpload, fileName + "." + fileExtension);
         File thumbFile = new File(fileName + ".png");
         boolean thumbNailCreated = FileUtils.createThumbnailFromVideo(fileName + "." + fileExtension, fileName + ".png");

         String originalURL = s3Utils.uploadFileToS3Bucket(bucketName, file, artistId + "/originals", fileName + "." + fileExtension);
         file.delete();

         String thumbnailURL = "https://postimg.cc/18TvM41x";
         if (thumbNailCreated) {
             thumbnailURL = s3Utils.uploadFileToS3Bucket(bucketName, thumbFile, artistId + "/thumbnails", fileName + ".png");
             thumbFile.delete();
         }

         ArtSample artSample = new ArtSample(
                 Constants.FALLBACK_ID,
                 artistId, originalURL,
                 thumbnailURL, caption, Constants.VIDEO,
                 new Timestamp(System.currentTimeMillis()));
         artistSampleRepository.save(artSample);
        return artSample;
    }

    private String getFilePath(String url) {
        String[] originalFileUrlSplit = url.split("/");
        originalFileUrlSplit = Arrays.copyOfRange(originalFileUrlSplit, 3, originalFileUrlSplit.length);
        return String.join("/", originalFileUrlSplit);
    }

    @Override
    public void deleteArtSample(String artistId, ArtInfo artInfo) {
         List<ArtSample> artSamples = artistSampleRepository.findByArtistId(artistId);
         for (ArtSample sample : artSamples) {
             if (sample.getOriginalUrl().equals(artInfo.getOriginalUrl())) {
                 artistSampleRepository.delete(sample);
                 break;
             }
         }

         String originalFilePath = getFilePath(artInfo.getOriginalUrl());
         String thumbnailFilePath = getFilePath(artInfo.getThumbnailUrl());

         s3Utils.removeFileFromS3Bucket(bucketName, originalFilePath);
         s3Utils.removeFileFromS3Bucket(bucketName, thumbnailFilePath);
    }

    @Override
    public List<ArtSample> getAllArtSamples(String slug) {
//         List<Artist> artist = artistRepository.findBySlug(slug);
//         if (artist.size() > 0) {
//             return artistSampleRepository.findByArtistId(artist.get(0).getArtistId());
//         }
        return new ArrayList<>();
    }
}
