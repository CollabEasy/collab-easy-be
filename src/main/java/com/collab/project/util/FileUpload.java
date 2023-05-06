package com.collab.project.util;

import com.collab.project.helpers.Constants;
import com.collab.project.model.artwork.UploadFile;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.HashSet;
import java.util.Set;

@Builder
@Component
@AllArgsConstructor
public class FileUpload {

    @Autowired
    S3Utils s3Utils;

    @NotNull
    String artistId;
    String fileName;
    @NotNull
    String fileType;
    String fileExtension;

    @NotNull
    String s3BucketName;
    @NotNull
    String s3Path;
    @NotNull
    MultipartFile fileToUpload;

    boolean shouldCreateThumbnailFile;
    boolean onlyUploadThumbnail;

    private File createThumbnailFileForVideo() throws IOException {
        File thumbFile = new File(fileName + ".png");
        boolean thumbNailCreated = FileUtils.createThumbnailFromVideo(fileName + "." + fileExtension, fileName + ".png");

        if (thumbNailCreated) {
           return thumbFile;
        }
        return null;
    }

    private String getThumbnailURL() throws IOException {
        File thumbFile = null;
        if (fileType.equals(Constants.VIDEO)) {
            thumbFile = createThumbnailFileForVideo();
            if (thumbFile == null) {
                return null;
            }
        } else {
            thumbFile = new File(fileName + "_thumb." + fileExtension);
        }
        String thumbnailURL = s3Utils.uploadFileToS3Bucket(s3BucketName, thumbFile, s3Path + "/thumbnails", fileName + ".png");
        thumbFile.delete();
        return thumbnailURL;
    }

    private UploadFile uploadFile() throws NoSuchAlgorithmException, IOException {
        // Creating original file
        fileName = Utils.getSHA256(artistId).substring(0, 15) + "_" + System.currentTimeMillis();
        File file = FileUtils.convertMultiPartFileToFile(fileToUpload, fileName + "." + fileExtension);

        // Creating thumbnail file
        String thumbnailUrl = null;
        if (shouldCreateThumbnailFile) {
            thumbnailUrl = getThumbnailURL();
            if (onlyUploadThumbnail) {
                return new UploadFile(null, thumbnailUrl);
            }
        }

        String originalURL = s3Utils.uploadFileToS3Bucket(s3BucketName, file, s3Path + "/originals", fileName + "." + fileExtension);
        file.delete();

        return new UploadFile(originalURL, thumbnailUrl);
    }

    public UploadFile checkFileTypeAndGetUploadURL() throws NoSuchAlgorithmException, IOException {
        fileExtension = FilenameUtils.getExtension(fileToUpload.getOriginalFilename());
        Set<String> allowedFormats = new HashSet();
        switch (fileType) {
            case Constants.AUDIO:
                allowedFormats = Constants.ALLOWED_AUDIO_FORMAT;
                break;
            case Constants.VIDEO:
                allowedFormats = Constants.ALLOWED_VIDEO_FORMAT;
                shouldCreateThumbnailFile = true;
                break;
            case Constants.IMAGE:
                allowedFormats = Constants.ALLOWED_IMAGE_FORMAT;
                shouldCreateThumbnailFile = true;
                break;
            default:
                throw new IllegalStateException("Invalid file type for upload");
        }
        if (!allowedFormats.contains(fileExtension)) {
            throw new IllegalStateException("Invalid file for selected file type. Allowed formats are "
                    + String.join(",", allowedFormats));
        }
        return this.uploadFile();
    }
}
