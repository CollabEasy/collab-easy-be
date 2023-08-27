package com.collab.project.service;

import com.collab.project.model.enums.Enums;
import com.collab.project.model.rewards.ReferralCodeResponse;
import com.collab.project.model.rewards.TotalPoints;

public interface RewardsService {

    ReferralCodeResponse fetchArtistWithReferralCode(String artistId, String referralCode);

    TotalPoints addPointsToUser(String artistSlug, String rewardType);

}
