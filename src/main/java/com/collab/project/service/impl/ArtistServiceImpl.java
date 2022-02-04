package com.collab.project.service.impl;

import com.collab.project.model.artist.Artist;
import com.collab.project.model.inputs.ArtistInput;
import com.collab.project.repositories.ArtistRepository;
import com.collab.project.service.ArtistService;
import com.collab.project.util.AuthUtils;

import java.sql.Timestamp;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;
import javax.persistence.Transient;

import io.jsonwebtoken.lang.Strings;
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

    private String getSlug(String firstName, String lastName) {
        String firstLastName = firstName.trim() + " " + lastName.trim();
        return Strings.replace(firstLastName.toLowerCase(Locale.ROOT), " ", "-");
    }

    @Override
    public Artist getArtistById(String artistId) {
        Artist artist = artistRepository.findByArtistId(artistId);
        return artist;
    }

    @Override
    public String getNewSlug(String slug) {
        String lastSlug = artistRepository.findLastSlugStartsWith(slug);
        if (lastSlug == null) return slug + "1";
        lastSlug = lastSlug.replace(slug, "");
        Integer lastNum = (lastSlug.equals("") ? 0 : Integer.valueOf(lastSlug)) + 1;
        return slug + "-" + lastNum;
    }

    @Override
    public Artist createArtist(ArtistInput inp) {
        Artist artist = artistRepository.findByEmail(inp.getEmail());
        if (Objects.isNull(artist)) {
            String slug = getSlug(inp.getFirstName(), inp.getLastName());
            String newSlug = getNewSlug(slug);
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
                    .slug(newSlug)
                    .createdAt(new Timestamp(System.currentTimeMillis()))
                .build();
            artist.setNewUser(true);
            artist = artistRepository.save(artist);
        }
        return artist;
    }

    @Override
    public Boolean updateArtist(ArtistInput inp) {
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
        artistRepository.save(artist);
        log.info("Update Artist Details with Id {}", artist.getArtistId());
        return true;
    }

    @Override
    @Transactional
    public void delete(ArtistInput artistInput) {
        artistRepository.deleteByArtistId(artistInput.getArtistId());
    }
}
