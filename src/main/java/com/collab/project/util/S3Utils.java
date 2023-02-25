package com.collab.project.util;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.time.LocalDateTime;

@Component
public class S3Utils {

     @Autowired
     private AmazonS3 amazonS3;

     public String uploadFileToS3Bucket(final String bucketName, final File file, String path, String fileName) {
         final String uniqueFileName = path + "/" + fileName;

         final PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, uniqueFileName, file).
                 withCannedAcl(CannedAccessControlList.PublicRead);
         amazonS3.putObject(putObjectRequest);
         return ((AmazonS3Client)amazonS3).getResourceUrl("artist-samples", uniqueFileName);
     }

     public void removeFileFromS3Bucket(final String bucketName, String filePath) {
         final DeleteObjectRequest deleteObjectRequest = new DeleteObjectRequest(bucketName, filePath);
         amazonS3.deleteObject(deleteObjectRequest);
     }
}
