package com.collab.project.controller;

import com.collab.project.model.enums.Enums;
import com.collab.project.model.inputs.ArtistInput;
import com.collab.project.model.response.SearchResponse;
import com.collab.project.model.response.SuccessResponse;
import com.collab.project.model.rewards.AwardInput;
import com.collab.project.model.rewards.ReferralCodeResponse;
import com.collab.project.model.rewards.TotalPoints;
import com.collab.project.service.RewardsService;
import com.collab.project.service.SearchService;
import com.collab.project.util.AuthUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/rewards")
public class RewardsController {
    @Autowired
    RewardsService rewardsService;

    @GetMapping
    @RequestMapping(value = "/referral/verify/{referralCode}", method = RequestMethod.GET)
    public ResponseEntity<SuccessResponse> verifyReferralCode(@PathVariable String referralCode) {
        String artistId = AuthUtils.getArtistId();
        ReferralCodeResponse response = rewardsService.fetchArtistWithReferralCode(artistId, referralCode);
        return new ResponseEntity<>(new SuccessResponse(response), HttpStatus.OK);
    }

    @PostMapping
    @RequestMapping(value = "/award", method = RequestMethod.POST)
    public ResponseEntity<SuccessResponse> verifyReferralCode(@RequestBody AwardInput input) {
        String rewardType = input.getRewardType();
        TotalPoints points = rewardsService.addPointsToUser(input.getArtistSlug(), input.getRewardType());
        return new ResponseEntity<>(new SuccessResponse(points), HttpStatus.OK);
    }


}
