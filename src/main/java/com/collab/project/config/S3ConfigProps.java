package com.collab.project.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("s3")
@Data
public class S3ConfigProps {
    String bucketName;

    String region;

}
