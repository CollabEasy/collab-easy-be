package com.collab.project.service.impl;

import com.collab.project.helpers.Constants;
import com.collab.project.model.artist.Artist;
import com.collab.project.model.artist.ArtistCategory;
import com.collab.project.model.contest.ContestSubmission;
import com.collab.project.model.enums.Enums;
import com.collab.project.repositories.*;
import com.collab.project.service.RewardsService;
import com.collab.project.util.Utils;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.security.NoSuchAlgorithmException;
import java.util.*;

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

    @Autowired
    ContestSubmissionRepository contestSubmissionRepository;

    @Autowired
    RewardsService rewardsService;

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

    public void backfillContestSubmissionPoints() throws JsonProcessingException {
        List<Artist> artists = artistRepository.findAll();

        for (Artist artist : artists) {
            Optional<List<ContestSubmission>> submissions = contestSubmissionRepository.findByArtistId(artist.getArtistId());
            if (!submissions.isPresent()) continue;
            for (ContestSubmission submission : submissions.get()) {
                String slug = submission.getContestSlug();
                Map<String, Object> details = new HashMap<String, Object>();
                details.put("contest_slug", slug);
                rewardsService.addPointsToUser(artist.getSlug(), Enums.RewardTypes.MONTHLY_CONTEST.toString(), details);
            }
        }
    }
}
