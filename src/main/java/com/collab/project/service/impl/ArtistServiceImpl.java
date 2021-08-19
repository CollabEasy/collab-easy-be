package com.collab.project.service.impl;

import com.collab.project.controller.ArtistInput;
import com.collab.project.model.artist.Artist;
import com.collab.project.repositories.ArtistRepository;
import com.collab.project.service.ArtistService;
import java.util.Objects;
import java.util.UUID;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ArtistServiceImpl implements ArtistService {

    @Autowired
    ArtistRepository artistRepository;
    @Override
    public Artist createArtist(ArtistInput inp) {
        Artist artist = artistRepository.findByArtistHandle(inp.getArtistHandle());
        if(Objects.isNull(artist)){
            artist = Artist.builder().
                artistId(UUID.randomUUID().toString())
                .artistHandle(inp.getArtistHandle())
                .age(inp.getAge())
                .email(inp.getEmail())
                .firstName(inp.getFirstName())
                .lastName(inp.getLastName())
                .country(inp.getCountry())
                .bio(inp.getBio())
                .profilePicUrl(inp.getProfilePicUrl())
                .timezone(inp.getTimezone())
                .phoneNumber(inp.getPhoneNumber())
                .build();
            artist = artistRepository.save(artist);
        }
        return artist;
    }

    @Override
    public void updateArtist() {

    }
}
