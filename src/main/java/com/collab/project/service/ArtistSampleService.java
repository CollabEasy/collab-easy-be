package com.collab.project.service;

import com.collab.project.model.art.ArtInfo;
import com.collab.project.model.artist.ArtSample;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public interface ArtistSampleService {

    ArtInfo uploadFile(String artistId, String caption, String fileType, MultipartFile fileToUpload) throws IOException, NoSuchAlgorithmException;

    void deleteArtSample(String artistId, ArtInfo artInfo);

    List<ArtSample> getAllArtSamples(String slug);
}
