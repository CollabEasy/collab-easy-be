package com.collab.project.service;

import com.collab.project.model.artist.ArtSample;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public interface ArtistSampleService {

    void uploadImage(String artistId, MultipartFile fileToUpload) throws IOException, NoSuchAlgorithmException;

    List<ArtSample> getAllArtSamples(String artistId);
}
