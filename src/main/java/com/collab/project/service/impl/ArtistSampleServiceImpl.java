package com.collab.project.service.impl;

import com.collab.project.helpers.Constants;
import com.collab.project.model.artist.ArtSample;
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
import java.util.List;

@Service
public class ArtistSampleServiceImpl implements ArtistSampleService {

    String bucketName = "wondor-samples";

    @Autowired
    S3Utils s3Utils;

    @Autowired
    ArtistSampleRepository artistSampleRepository;


    @Async
    @Override
    public void uploadFile(String artistId, String fileType, MultipartFile fileToUpload) throws IOException, NoSuchAlgorithmException {
        String fileExtension = FilenameUtils.getExtension(fileToUpload.getOriginalFilename());
        switch (fileType) {
            case Constants.AUDIO:
                if (!Constants.ALLOWED_AUDIO_FORMAT.contains(fileExtension)) {
                    throw new IllegalStateException("Invalid file extension");
                }
                uploadAudio(artistId, fileToUpload, fileExtension);
                break;
            case Constants.VIDEO:
                if (!Constants.ALLOWED_VIDEO_FORMAT.contains(fileExtension)) {
                    throw new IllegalStateException("Invalid file extension");
                }
                uploadVideo(artistId, fileToUpload, fileExtension);
                break;
            case Constants.IMAGE:
                if (!Constants.ALLOWED_IMAGE_FORMAT.contains(fileExtension)) {
                    throw new IllegalStateException("Invalid file extension");
                }
                uploadImage(artistId, fileToUpload, fileExtension);
                break;
            default:
                throw new IllegalStateException("Invalid file type for upload");
        }
    }

    private void uploadImage(String artistId, MultipartFile fileToUpload, String fileExtension) throws NoSuchAlgorithmException, IOException {
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
        ArtSample artSample = new ArtSample(Constants.FALLBACK_ID, artistId, originalURL, thumbnailURL, new Timestamp(System.currentTimeMillis()));
        artistSampleRepository.save(artSample);
    }

    private void uploadAudio(String artistId, MultipartFile fileToUpload, String fileExtension) throws NoSuchAlgorithmException, IOException {
        String fileName = Utils.getSHA256(artistId).substring(0, 15) + "_" + System.currentTimeMillis();
        File file = FileUtils.convertMultiPartFileToFile(fileToUpload, fileName + "." + fileExtension);

        String fileURL = s3Utils.uploadFileToS3Bucket(bucketName, file, artistId + "/originals", fileName + "." + fileExtension);
        file.delete();

        ArtSample artSample = new ArtSample(Constants.FALLBACK_ID, artistId, fileURL, fileURL, new Timestamp(System.currentTimeMillis()));
        artistSampleRepository.save(artSample);
    }

    private void uploadVideo(String artistId, MultipartFile fileToUpload, String fileExtension) throws NoSuchAlgorithmException, IOException {
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

        ArtSample artSample = new ArtSample(Constants.FALLBACK_ID, artistId, originalURL, thumbnailURL, new Timestamp(System.currentTimeMillis()));
        artistSampleRepository.save(artSample);
    }

    @Override
    public List<ArtSample> getAllArtSamples(String artistId) {
        return artistSampleRepository.findByArtistId(artistId);
    }
}
