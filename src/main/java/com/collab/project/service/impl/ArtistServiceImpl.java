package com.collab.project.service.impl;

import com.collab.project.model.artist.Artist;
import com.collab.project.model.inputs.ArtistInput;
import com.collab.project.repositories.ArtistRepository;
import com.collab.project.service.ArtistService;
import com.collab.project.util.AuthUtils;
import java.util.Objects;
import java.util.UUID;
import javax.persistence.Transient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@Slf4j
public class ArtistServiceImpl implements ArtistService {

    @Autowired
    ArtistRepository artistRepository;

    @Autowired
    AuthUtils authUtils;

    @Override
    public Artist createArtist(ArtistInput inp) {
        Artist artist = artistRepository.findByEmail(inp.getEmail());
        if (Objects.isNull(artist)) {
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
            artist.setNewUser(true);
        }
        return artist;
    }

    @Override
    public Artist updateArtist(ArtistInput inp) {
        Artist artist = artistRepository.findByArtistId(AuthUtils.getArtistId());
        if (Objects.nonNull(inp.getPhoneNumber()) && inp.getPhoneNumber() > 0) {
            artist.setPhoneNumber(inp.getPhoneNumber());
        }
        if (Objects.nonNull(inp.getAge()) && inp.getAge() > 0) {
            artist.setAge(inp.getAge());
        }
        if (!StringUtils.isEmpty(inp.getCountry())) {
            artist.setCountry(inp.getCountry());
        }
        if (!StringUtils.isEmpty(inp.getTimezone())) {
            artist.setTimezone(inp.getTimezone());
        }
        if (!StringUtils.isEmpty(inp.getBio())) {
            artist.setBio(inp.getBio());
        }
        if (!StringUtils.isEmpty(inp.getGender())) {
            artist.setGender(inp.getGender());
        }

        if (!StringUtils.isEmpty(inp.getFirstName())) {
            artist.setFirstName(inp.getFirstName());
        }
        if (!StringUtils.isEmpty(inp.getLastName())) {
            artist.setLastName(inp.getLastName());
        }

        artist = artistRepository.save(artist);
        log.info("Update Artist Details with Id {}", artist.getArtistId());
        return artist;
    }

    @Override
    @Transactional
    public void delete(ArtistInput artistInput) {
        artistRepository.deleteByArtistId(artistInput.getArtistId());
    }

    @Override
    public Artist getArtist() {
        return artistRepository.findByEmail(authUtils.getEmail());
    }
}
