package com.collab.project.service.impl;

import com.collab.project.model.artist.Artist;
import com.collab.project.model.artist.ArtistCategory;
import com.collab.project.repositories.ArtistCategoryRepository;
import com.collab.project.repositories.ArtistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScriptServiceImpl {

    @Autowired
    ArtistRepository artistRepository;

    @Autowired
    ArtistCategoryRepository artistCategoryRepository;

    public void updateProfileCompleteStatus() {
        List<Artist> artists = artistRepository.findAll();

        for (Artist artist : artists) {
            if (artist.getBio() == null || artist.getBio().isEmpty()) continue;
            List<ArtistCategory> categories = artistCategoryRepository.findByArtistId(artist.getArtistId());
            if (categories.isEmpty()) continue;
            artist.setProfileComplete(true);
            artistRepository.save(artist);
        }
    }
}
