package com.collab.project.service.impl;

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
    @Autowired
    private ArtistSocialProspectusRepository artistSocialProspectusRepository;

    @Autowired
    private ArtistRepository artistRepository;

    @Override
    public ArtistSocialProspectus createArtistSocialProspectus(ArtistSocialProspectus artistSocialProspectus) {
        ArtistSocialProspectus prospectus = new ArtistSocialProspectus(FALLBACK_ID, AuthUtils.getArtistId(),
                artistSocialProspectus.getSocialPlatformId(), artistSocialProspectus.getHandle(),
                artistSocialProspectus.getDescription());

        System.out.println("Rabbal is saving in DB");
        return artistSocialProspectusRepository.save(prospectus);
    }


    @Override
    public void delete(ArtistSocialProspectusInput artistSocialProspectusInput) {

    }

    @Override
    public List<ArtistSocialProspectus> getSocialProspectByArtistId(String artistId) {
        List<ArtistSocialProspectus> prospectus = artistSocialProspectusRepository.findByArtistId(artistId);
        return prospectus;
    }
}
