package com.collab.project.service.impl;

import com.collab.project.helpers.Constants;
import com.collab.project.model.artist.ArtSample;
import com.collab.project.repositories.ArtistSampleRepository;
import com.collab.project.service.ArtistSampleService;
import com.collab.project.util.FileUtils;
import com.collab.project.util.S3Utils;
import com.collab.project.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import static com.vladmihalcea.hibernate.type.util.LogUtils.LOGGER;

@Service
public class ArtistSampleServiceImpl implements ArtistSampleService {

    String bucketName = "wondor-samples";

    @Autowired
    S3Utils s3Utils;

    @Autowired
    ArtistSampleRepository artistSampleRepository;


    @Async
    @Override
    public void uploadImage(String artistId, MultipartFile fileToUpload) throws IOException, NoSuchAlgorithmException {
        String fileName = Utils.getSHA256(artistId).substring(0, 15) + "_" + System.currentTimeMillis();
        File file = FileUtils.convertMultiPartFileToFile(fileToUpload, fileName + ".jpg");

        FileUtils.createThumbnail(fileToUpload, fileName + "_thumb.jpg");
        File thumbFile = new File(fileName + "_thumb.jpg");

        String originalURL = s3Utils.uploadFileToS3Bucket(bucketName, file, artistId + "/originals", fileName + ".jpg");
        String thumbnailURL = s3Utils.uploadFileToS3Bucket(bucketName, thumbFile, artistId + "/thumbnails", fileName + ".jpg");

        thumbFile.delete();
        file.delete();
        ArtSample artSample = new ArtSample(Constants.FALLBACK_ID, artistId, originalURL, thumbnailURL, new Timestamp(System.currentTimeMillis()));
        artistSampleRepository.save(artSample);
    }

    @Override
    public List<ArtSample> getAllArtSamples(String artistId) {
        List<ArtSample> artSamples = artistSampleRepository.findByArtistId(artistId);
        return artSamples;
    }
}
