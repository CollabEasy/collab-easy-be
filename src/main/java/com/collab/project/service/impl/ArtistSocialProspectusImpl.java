package com.collab.project.service.impl;

import com.collab.project.helpers.Constants;
import com.collab.project.model.artist.Artist;
import com.collab.project.model.enums.Enums;
import com.collab.project.model.inputs.ArtistSocialProspectusInput;
import com.collab.project.model.socialprospectus.ArtistSocialProspectus;
import com.collab.project.repositories.ArtistRepository;
import com.collab.project.repositories.ArtistSocialProspectusRepository;
import com.collab.project.repositories.SocialPlatformRepository;
import com.collab.project.service.ArtistSocialProspectusService;
import com.collab.project.util.AuthUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.collab.project.helpers.Constants.FALLBACK_ID;

@Service
public class ArtistSocialProspectusImpl implements ArtistSocialProspectusService {

    String socialKey = "SOCIAL";
    @Autowired
    private ArtistSocialProspectusRepository artistSocialProspectusRepository;

    @Autowired
    private ArtistRepository artistRepository;

    @Override
    public ArtistSocialProspectus addArtistSocialProspectus(ArtistSocialProspectusInput artistSocialProspectusInput) {
        ArtistSocialProspectus prospectus = artistSocialProspectusRepository.findByArtistAndPlatformId(AuthUtils.getArtistId(), artistSocialProspectusInput.getSocialPlatformId());
        if (prospectus == null) {
            prospectus = new ArtistSocialProspectus(FALLBACK_ID, AuthUtils.getArtistId(),
                    artistSocialProspectusInput.getSocialPlatformId(), artistSocialProspectusInput.getHandle(),
                    artistSocialProspectusInput.getDescription(), artistSocialProspectusInput.getUpForCollab());
        } else {
            prospectus.setDescription(artistSocialProspectusInput.getDescription());
            prospectus.setHandle(artistSocialProspectusInput.getHandle());
            prospectus.setUpForCollab(artistSocialProspectusInput.getUpForCollab());
        }
        Artist artist = artistRepository.findByArtistId(AuthUtils.getArtistId());
        boolean isIncomplete = artist.getProfileIncomplete() == false;
        if ((artist.getProfileBits() >> (Constants.profileBits.get(socialKey)) % 2) == 0) {
            artist.setProfileBits(artist.getProfileBits() | (1 << Constants.profileBits.get(socialKey)));
            if (artist.getProfileBits() == Constants.ALL_PROFILE_BIT_SET) {
                artist.setProfileComplete(true);
            }
            if (isIncomplete) {
                rewardsService.addPointsToUser(artist.getSlug(), Constants.RewardPoints.get(Enums.RewardTypes.PROFILE_COMPLETION), null);
            }
            artistRepository.save(artist);
        }
        return artistSocialProspectusRepository.save(prospectus);
    }

    @Override
    public List<ArtistSocialProspectus> getSocialProspectByArtistId(String artistId) {
        List<ArtistSocialProspectus> prospectus = artistSocialProspectusRepository.findByArtistId(artistId);
        return prospectus;
    }

    @Override
    public  boolean deleteSocialProspectus(String artistId, Long platformId) {
        ArtistSocialProspectus prospectus = artistSocialProspectusRepository.findByArtistAndPlatformId(artistId, platformId);
        if (prospectus != null) {
            artistSocialProspectusRepository.delete(prospectus);
        }
        return true;
    }
}
