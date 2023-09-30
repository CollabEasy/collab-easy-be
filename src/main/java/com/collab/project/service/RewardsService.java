package com.collab.project.service;

import com.collab.project.model.artist.Artist;
import com.collab.project.model.enums.Enums;
import com.collab.project.model.rewards.ReferralCodeResponse;
import com.collab.project.model.rewards.RewardsActivity;
import com.collab.project.model.rewards.TotalPoints;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;
import java.util.Map;

public interface RewardsService {

    ReferralCodeResponse fetchArtistWithReferralCode(String artistId, String referralCode) throws JsonProcessingException;

    TotalPoints addPointsToUserByArtist(Artist artist, Enums.RewardTypes rewardType, Map<String, Object> details) throws JsonProcessingException;

    TotalPoints addPointsToUser(String artistSlug, String rewardType, Map<String, Object> details) throws JsonProcessingException;

    void updateReferralCodeField(String artistID);

    List<RewardsActivity> getRewardsActivity(String artistID);

    TotalPoints getPoints(String artistID);

}
