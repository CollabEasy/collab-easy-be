package com.collab.project.service.impl;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class RewardsServiceImpl implements RewardsService {

    @Autowired
    ArtistRepository artistRepository;

    @Autowired
    RewardsActivityRepository rewardsActivityRepository;

    @Autowired
    TotalPointsRepository totalPointsRepository;

    @Override
    public ReferralCodeResponse fetchArtistWithReferralCode(String artistId, String referralCode) {
        List<Artist> artist = artistRepository.findByReferralCode(referralCode);
        if (artist.isEmpty() || artist.size() > 1) {
            return new ReferralCodeResponse(false, null, null, null);
        }
        Artist referrer = artist.get(0);
        return new ReferralCodeResponse(true, referrer.getFirstName(), referrer.getLastName(), referrer.getSlug());
    }

    @Override
    public TotalPoints addPointsToUser(String artistSlug, String rewardType) {
        Enums.RewardTypes rewardFor = Enums.RewardTypes.valueOf(rewardType);
        Integer points = Constants.RewardPoints.getOrDefault(rewardFor, 50);
        Artist artist = artistRepository.findBySlug(artistSlug).get(0);
        rewardsActivityRepository.save(new RewardsActivity(Timestamp.from(Instant.now()), artist.getArtistId(),
                rewardType.toString(),
                points, true, null));
        Optional<TotalPoints> totalPointsOptional = totalPointsRepository.findByArtistId(artist.getArtistId());
        TotalPoints totalPoints = totalPointsOptional.orElseGet(() -> new TotalPoints(artist.getArtistId(), 0));
        totalPoints.setTotalPoints(totalPoints.getTotalPoints() + points);
        totalPointsRepository.save(totalPoints);
        return totalPoints;
    }
}
