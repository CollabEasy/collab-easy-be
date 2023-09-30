package com.collab.project.service.impl;

import com.collab.project.helpers.Constants;
import com.collab.project.model.artist.Artist;
import com.collab.project.model.artist.ArtistCategory;
import com.collab.project.repositories.ArtistCategoryRepository;
import com.collab.project.repositories.ArtistRepository;
import com.collab.project.repositories.ArtistSampleRepository;
import com.collab.project.repositories.ArtistSocialProspectusRepository;
import com.collab.project.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Locale;

@Service
public class ScriptServiceImpl {

    @Autowired
    ArtistRepository artistRepository;

    @Autowired
    ArtistCategoryRepository artistCategoryRepository;

    @Autowired
    ArtistSampleRepository artistSampleRepository;

    @Autowired
    ArtistSocialProspectusRepository artistSocialProspectusRepository;

    public void updateProfileCompleteStatus() {
        List<Artist> artists = artistRepository.findAll();

        for (Artist artist : artists) {
            int profileBits = 0;
            if (artist.getBio() != null && !StringUtils.isEmpty(artist.getBio())) {
                profileBits = profileBits | (1 << Constants.profileBits.get("BIO"));
            }

            if (!artistSampleRepository.findByArtistId(artist.getArtistId()).isEmpty()) {
                profileBits = profileBits | (1 << Constants.profileBits.get("SAMPLES"));
            }

            if (!artistSocialProspectusRepository.findByArtistId(artist.getArtistId()).isEmpty()) {
                profileBits = profileBits | (1 << Constants.profileBits.get("SOCIAL"));
            }

            artist.setProfileBits(profileBits);
            artist.setProfileComplete(profileBits == Constants.ALL_PROFILE_BIT_SET);
            artistRepository.save(artist);
        }
    }

    public void backfillReferralCodes() throws NoSuchAlgorithmException {
        List<Artist> artists = artistRepository.findAll();

        for (Artist artist : artists) {
            String referralCode =
                    (artist.getFirstName() + artist.getLastName()).substring(0, 4).toUpperCase(Locale.ROOT)
                    + "-"
                    + Utils.getSHA256(artist.getSlug()).substring(0, 4).toUpperCase();
            artist.setReferralCode(referralCode);
            artistRepository.save(artist);
        }
    }
}
