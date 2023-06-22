package com.collab.project.service.impl;

import com.collab.project.model.artist.Artist;
import com.collab.project.repositories.ArtistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ArtistGroupServiceImpl {

    @Autowired
    ArtistRepository artistRepository;


    public List<String> getEmailListWithIncompleteProfile() {
        List<Artist> artists = artistRepository.findAll();
        List<String> requiredArtistsEmail = new ArrayList<>();
        for (Artist artist : artists) {
            if (!artist.getProfileComplete()) {
                requiredArtistsEmail.add(artist.getEmail());
            }
        }
        return requiredArtistsEmail;
    }
}
