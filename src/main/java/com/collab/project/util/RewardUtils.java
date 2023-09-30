package com.collab.project.util;

import com.collab.project.helpers.Constants;
import com.collab.project.model.artist.Artist;
import com.collab.project.model.enums.Enums;
import com.collab.project.repositories.ArtistRepository;
import com.collab.project.service.RewardsService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RewardUtils {

    @Autowired
    ArtistRepository artistRepository;

    @Autowired
    RewardsService rewardsService;

    public void addPointsIfProfileComplete(Artist artist, String actionKey) throws JsonProcessingException {
        boolean isComplete = artist.getProfileComplete();
        System.out.println("complete : " + isComplete);
        if (isComplete) return;
        if (((artist.getProfileBits() >> Constants.profileBits.get(actionKey)) % 2) == 0) {
            System.out.println("bits  : " + artist.getProfileBits() + " " + (1 << Constants.profileBits.get(actionKey)) + " , " + (artist.getProfileBits() || (1 << Constants.profileBits.get(actionKey))));
            artist.setProfileBits(artist.getProfileBits() || (1 << Constants.profileBits.get(actionKey)));
            if (artist.getProfileBits() == Constants.ALL_PROFILE_BIT_SET) {
                artist.setProfileComplete(true);
                rewardsService.addPointsToUserByArtist(artist, Enums.RewardTypes.PROFILE_COMPLETION, null);
            }
            System.out.println("setting points : " + artist.getProfileBits());
            artistRepository.save(artist);
        }
    }
}
