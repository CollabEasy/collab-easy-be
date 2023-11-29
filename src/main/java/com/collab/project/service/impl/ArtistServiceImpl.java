package com.collab.project.service.impl;

import com.collab.project.email.EmailService;
import com.collab.project.helpers.Constants;
import com.collab.project.model.artist.Artist;
import com.collab.project.model.artist.ArtistCategory;
import com.collab.project.model.artist.BasicArtist;
import com.collab.project.model.artwork.UploadFile;
import com.collab.project.model.inputs.ArtistInput;
import com.collab.project.model.socialprospectus.ArtistSocialProspectus;
import com.collab.project.repositories.ArtistRepository;
import com.collab.project.service.ArtistCategoryService;
import com.collab.project.service.ArtistService;
import com.collab.project.service.ArtistSocialProspectusService;
import com.collab.project.util.*;
import com.collab.project.util.emailTemplates.NewUserEmail;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.jsonwebtoken.lang.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

@Service
@Slf4j
public class ArtistServiceImpl implements ArtistService {

    String bucketName = "wondor-profile-pictures";

    String bioKey = "BIO";

    @Autowired
    ArtistRepository artistRepository;

    @Autowired
    ArtistCategoryService artistCategoryService;

    @Autowired
    ArtistSocialProspectusService artistSocialProspectusService;

    @Autowired
    S3Utils s3Utils;

    @Autowired
    EmailService emailService;
    @Autowired
    RewardUtils rewardUtils;

    private String getSlug(String firstName, String lastName) {
        String firstLastName = firstName.trim() + " " + lastName.trim();
        return Strings.replace(firstLastName.toLowerCase(Locale.ROOT), " ", "-");
    }

    @Override
    public Artist getArtistById(String artistId) {
        Artist artist = artistRepository.findByArtistId(artistId);
        return artist;
    }

    public String getNewSlug(String slug) {
        String lastSlug = artistRepository.findLastSlugStartsWith(slug);
        if (lastSlug == null)
            return slug + "1";
        lastSlug = lastSlug.replace(slug, "");
        Integer lastNum = (lastSlug.equals("") ? 0 : Integer.valueOf(lastSlug)) + 1;
        return slug + lastNum;
    }

    @Override
    public Artist getArtistBySlug(String slug) {
        List<Artist> artists = artistRepository.findBySlug(slug);
        if (artists.size() > 0)
            return artists.get(0);
        return null;
    }

    @Override
    public Artist createArtist(ArtistInput inp) throws NoSuchAlgorithmException {
        Artist artist = artistRepository.findByEmail(inp.getEmail());
        if (Objects.isNull(artist)) {
            String slug = getSlug(inp.getFirstName(), inp.getLastName());
            String newSlug = getNewSlug(slug + "-");
            String referralCode = ((inp.getFirstName() + inp.getLastName())).substring(0, 4).toUpperCase(Locale.ROOT)
                    + "-"
                    + Utils.getSHA256(newSlug).substring(0, 4).toUpperCase();
            artist =
                    Artist.builder()
                            .artistId(UUID.randomUUID().toString())
                            .artistHandle(inp.getArtistHandle())
                            .age(inp.getAge())
                            .bio("")
                            .email(inp.getEmail())
                            .firstName(inp.getFirstName())
                            .lastName(inp.getLastName())
                            .country(inp.getCountry())
                            .state(inp.getState())
                            .city(inp.getCity())
                            .countryDial(inp.getCountryDial())
                            .countryIso(inp.getCountryIso())
                            .bio(inp.getBio() == null ? "" : inp.getBio())
                            .profilePicUrl(inp.getProfilePicUrl())
                            .timezone(inp.getTimezone())
                            .phoneNumber(inp.getPhoneNumber())
                            .slug(newSlug)
                            .referralCode(referralCode)
                            .build();
            artist.setNewUser(true);
            artist.setTestUser(false);
            artist.setIsReferralDone(false);
            artist.setProfileComplete(false);
            artist.setProfileBits(0);
            artist = artistRepository.save(artist);
            try {
                emailService.sendEmailFromStringFinal(
                        "Welcome to Wondor",
                        artist.getArtistId(),
                        artist.getEmail(),
                        NewUserEmail.getContent(artist.getFirstName()),
                        false
                );
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return artist;
    }

    @Override
    public Boolean updateArtist(ArtistInput inp) throws JsonProcessingException {
        Artist artist = artistRepository.findByArtistId(AuthUtils.getArtistId());
        Integer profileCompletedBits = artist.getProfileBits();
        boolean isIncomplete = artist.getProfileComplete() == false;
        if (Objects.nonNull(inp.getFirstName()) && inp.getFirstName().length() > 0) {
            artist.setFirstName(inp.getFirstName());
        }
        if (Objects.nonNull(inp.getLastName()) && inp.getLastName().length() > 0) {
            artist.setLastName(inp.getLastName());
        }
        if (Objects.nonNull(inp.getPhoneNumber()) && inp.getPhoneNumber() > 0) {
            artist.setPhoneNumber(inp.getPhoneNumber());
        }
        if (Objects.nonNull(inp.getAge()) && inp.getAge() > 0) {
            artist.setAge(inp.getAge());
        }
        if (!StringUtils.isEmpty(inp.getCountry())) {
            artist.setCountry(inp.getCountry());
        }
        if (!StringUtils.isEmpty(inp.getCountryDial())) {
            artist.setCountry(inp.getCountryDial());
        }
        if (!StringUtils.isEmpty(inp.getCountryIso())) {
            artist.setCountry(inp.getCountryIso());
        }
        if (!StringUtils.isEmpty(inp.getTimezone())) {
            artist.setTimezone(inp.getTimezone());
        }
        if (!StringUtils.isEmpty(inp.getBio())) {
            artist.setBio(inp.getBio());
            rewardUtils.addPointsIfProfileComplete(artist, bioKey);
        }
        if (!StringUtils.isEmpty(inp.getGender())) {
            artist.setGender(inp.getGender());
        }
        if (inp.getDateOfBirth() != null) {
            artist.setDateOfBirth(inp.getDateOfBirth());
        }

        // Save state and city as is.
        artist.setState(inp.getState());
        artist.setCity(inp.getCity());

        artistRepository.save(artist);
        log.info("Update Artist Details with Id {}", artist.getArtistId());
        return true;
    }

    @Override
    @Transactional
    public void delete(ArtistInput artistInput) {
        artistRepository.deleteByArtistId(artistInput.getArtistId());
    }

    @Override
    public Artist updateProfilePicture(String artistId, MultipartFile filename) throws NoSuchAlgorithmException,
            IOException {
        String time = String.valueOf(System.currentTimeMillis());
        String artistFileName = Utils.getSHA256(artistId).substring(0, 15);

        FileUpload fileUploadBuilder =
                FileUpload.builder()
                        .s3Utils(s3Utils)
                        .fileToUpload(filename)
                        .fileName(artistFileName)
                        .artistId(artistId)
                        .s3BucketName(bucketName)
                        .s3Path("")
                        .fileType(Constants.IMAGE)
                        .onlyUploadThumbnail(true).build();

        UploadFile uploadedFile = fileUploadBuilder.checkFileTypeAndGetUploadURL();

        Artist artist = artistRepository.findByArtistId(artistId);
        artist.setProfilePicUrl(uploadedFile.getThumbnailURL() + "?updatedAt=" + time);
        return artistRepository.save(artist);
    }

    @Override
    public Boolean isProfileComplete(String artistId) {
        Artist artist = artistRepository.findByArtistId(artistId);
        return artist.getProfileComplete();
    }

    @Override
    public BasicArtist getBasicArtist(String slug) {
        List<Artist> artists = artistRepository.findBySlug(slug);
        if (artists.isEmpty()) {
            return new BasicArtist();
        }
        Artist artist = artists.get(0);
        List<String> category = artistCategoryService.getArtistCategories(artist.getArtistId());
        return new BasicArtist(artist, category);
    }
}
