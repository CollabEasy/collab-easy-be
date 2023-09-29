package com.collab.project.service;


import com.collab.project.model.artist.Artist;
import com.collab.project.model.inputs.ArtistInput;
import com.collab.project.util.AuthUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public interface ArtistService {

    public Artist createArtist(ArtistInput artistInput) throws NoSuchAlgorithmException;

    public Boolean updateArtist(ArtistInput artistInput) throws JsonProcessingException;

    public void delete(ArtistInput artistInput);

    public Artist getArtistById(String artistId);

    Artist getArtistBySlug(String handle);

    Artist updateProfilePicture(String artistId, MultipartFile filename) throws NoSuchAlgorithmException, IOException;
}
