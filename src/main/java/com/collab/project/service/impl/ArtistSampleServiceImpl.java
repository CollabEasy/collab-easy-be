package com.collab.project.service.impl;

import com.collab.project.helpers.Constants;
import com.collab.project.model.art.ArtInfo;
import com.collab.project.model.artist.ArtSample;
import com.collab.project.model.artist.Artist;
import com.collab.project.model.artwork.UploadFile;
import com.collab.project.repositories.ArtistRepository;
import com.collab.project.repositories.ArtistSampleRepository;
import com.collab.project.service.ArtistSampleService;
import com.collab.project.util.FileUpload;
import com.collab.project.util.S3Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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
        FileUpload fileUploadHelper =
                FileUpload.builder().artistId(artistId).fileToUpload(fileToUpload).s3BucketName(bucketName).fileType(fileType).s3Path(artistId).build();

        UploadFile uploadedFile = fileUploadHelper.checkFileTypeAndGetUploadURL();

        ArtSample artSample = new ArtSample(Constants.FALLBACK_ID, artistId, uploadedFile.getOriginalURL(),
                uploadedFile.getThumbnailURL(), caption, Constants.IMAGE, new Timestamp(System.currentTimeMillis()));

        artistSampleRepository.save(artSample);

        return new ArtInfo(artSample.getCaption(), artSample.getFileType(), artSample.getOriginalUrl(),
                artSample.getThumbnailUrl(), artSample.getCreatedAt());
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
        List<Artist> artist = artistRepository.findBySlug(slug);
        if (artist.size() > 0) {
            return artistSampleRepository.findByArtistId(artist.get(0).getArtistId());
        }
        return new ArrayList<>();
    }
}
