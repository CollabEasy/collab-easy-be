package com.collab.project.service.impl;

import com.collab.project.exception.RecordNotFoundException;
import com.collab.project.helpers.Constants;
import com.collab.project.model.artist.Artist;
import com.collab.project.model.enums.Enums;
import com.collab.project.model.rewards.ReferralCodeResponse;
import com.collab.project.model.rewards.RewardsActivity;
import com.collab.project.model.rewards.TotalPoints;
import com.collab.project.repositories.ArtistRepository;
import com.collab.project.repositories.RewardsActivityRepository;
import com.collab.project.repositories.TotalPointsRepository;
import com.collab.project.service.RewardsService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;

@Service
public class RewardsServiceImpl implements RewardsService {

    @Autowired
    ArtistRepository artistRepository;

    @Autowired
    RewardsActivityRepository rewardsActivityRepository;

    @Autowired
    TotalPointsRepository totalPointsRepository;

    @Override
    public ReferralCodeResponse fetchArtistWithReferralCode(String artistId, String referralCode) throws JsonProcessingException {
        Artist current = artistRepository.getOne(artistId);
        if (current.getIsReferralDone()) {
            throw new IllegalStateException("You cannot claim referral code after claiming/skipping once.");
        }

        List<Artist> artist = artistRepository.findByReferralCode(referralCode);
        if (artist.size() != 1) {
            throw new RecordNotFoundException("The referral code is not valid");
        }
        Artist referrer = artist.get(0);
        Map<String, Object> details = new HashMap<>();
        details.put("referred_by", referrer.getArtistId());
        addPointsByArtistId(artistId, Enums.RewardTypes.REFERRAL_USER, details);
        details.clear();
        details.put("referred_to", artistId);
        addPointsByArtistId(referrer.getSlug(), Enums.RewardTypes.REFERRAL_SHARER, details);
        current.setIsReferralDone(true);
        artistRepository.save(current);
        return new ReferralCodeResponse(true, referrer.getFirstName(), referrer.getLastName(), referrer.getSlug());
    }

    @Override
    public TotalPoints addPointsToUser(String artistSlug, String rewardType, Map<String, Object> details) throws JsonProcessingException {
        Enums.RewardTypes rewardFor = Enums.RewardTypes.valueOf(rewardType);
        Artist artist = artistRepository.findBySlug(artistSlug).get(0);
        return addPointsByArtistId(artist.getArtistId(), rewardFor, details);
    }

    @Override
    public void updateReferralCodeField(String artistID) {
        Artist artist = artistRepository.getOne(artistID);
        artist.setIsReferralDone(true);
        artistRepository.save(artist);
    }

    @Override
    public List<RewardsActivity> getRewardsActivity(String artistID) {
        Optional<List<RewardsActivity>> list = rewardsActivityRepository.findByArtistId(artistID);
        return list.orElseGet(ArrayList::new);
    }

    private TotalPoints addPointsByArtistId(String artistID, Enums.RewardTypes rewardType,
                                            Map<String, Object> details) throws JsonProcessingException {
        Integer points = Constants.RewardPoints.getOrDefault(rewardType, 50);
        String detailString = (new ObjectMapper()).writeValueAsString(details);
        rewardsActivityRepository.save(new RewardsActivity(Timestamp.from(Instant.now()), artistID,
                rewardType.toString(),
                points, true, detailString));
        Optional<TotalPoints> totalPointsOptional = totalPointsRepository.findByArtistId(artistID);
        TotalPoints totalPoints = totalPointsOptional.orElseGet(() -> new TotalPoints(artistID, 0, 0));
        totalPoints.setTotalPoints(totalPoints.getTotalPoints() + points);
        totalPoints.setLifetimePoints(totalPoints.getLifetimePoints() + points);;
        totalPointsRepository.save(totalPoints);
        return totalPoints;
    }
}
