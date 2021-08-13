package com.collab.project.service.impl;

import com.collab.project.model.inputs.ArtistInput;
import com.collab.project.model.artist.Artist;
import com.collab.project.repositories.ArtistRepository;
import com.collab.project.service.ArtistService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ArtistServiceImpl implements ArtistService {

    @Autowired
    ArtistRepository artistRepository;
    @Override
    public void createArtist(ArtistInput artistInput) {
        Artist artist = new Artist();
        BeanUtils.copyProperties(artistInput, artist);
        artistRepository.save(artist);
    }

    @Override
    public void updateArtist() {

    }
}
