package com.collab.project.util;

import com.collab.project.helpers.Constants;
import lombok.extern.slf4j.Slf4j;
import org.imgscalr.Scalr;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Objects;

@Slf4j
public class FileUtils {
    public static File convertMultiPartFileToFile(final MultipartFile multipartFile, String fileName) throws IOException {
        final File file = new File(fileName);
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(file);
            outputStream.write(multipartFile.getBytes());
        } finally {
            Objects.requireNonNull(outputStream).close();
        }
        return file;
    }

    public static void createThumbnail(MultipartFile originalFile, String fileName) throws IOException{
        ByteArrayOutputStream thumbOutput = new ByteArrayOutputStream();
        BufferedImage thumbImg = null;
        BufferedImage img = ImageIO.read(originalFile.getInputStream());
        thumbImg = Scalr.resize(img, Scalr.Method.AUTOMATIC, Constants.THUMBNAIL_WIDTH, Constants.THUMBNAIL_HEIGHT, Scalr.OP_ANTIALIAS);
        ImageIO.write(thumbImg, Objects.requireNonNull(originalFile.getContentType()).split("/")[1] , thumbOutput);
        convertBytesToFile(thumbOutput, fileName);
    }

    private static void convertBytesToFile(ByteArrayOutputStream byteArrayOutputStream, String fileName) throws IOException {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(fileName);
            byteArrayOutputStream.writeTo(fos);
        } finally {
            Objects.requireNonNull(fos).close();
        }
    }

}
