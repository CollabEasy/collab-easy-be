package com.collab.project.service.impl;

import com.collab.project.model.inputs.ArtistSocialProspectusInput;
import com.collab.project.model.socialprospectus.ArtistSocialProspectus;
import com.collab.project.repositories.ArtistRepository;
import com.collab.project.repositories.ArtistSocialProspectusRepository;
import com.collab.project.repositories.SocialPlatformRepository;
import com.collab.project.service.ArtistSocialProspectusService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class ArtistSocialProspectusServiceImpl implements ArtistSocialProspectusService {
    @Autowired
    private ArtistSocialProspectusRepository artistSocialProspectusRepository;

    @Autowired
    private ArtistRepository artistRepository;

    public ArtistSocialProspectus createSocialPlatform(ArtistSocialProspectusInput artistSocialProspectusInput) {

    }

    public Boolean updateSocialPlatform(ArtistSocialProspectusInput artistSocialProspectusInput) {

    }

    public void delete(ArtistSocialProspectusInput artistSocialProspectusInput) {

    }

    public List<ArtistSocialProspectus> getSocialProspectByArtistId(String artistId) {
        artistSocialProspectusRepository artistRepository.findByArtistId();

    }

    public ArtistSocialProspectus getSocialProspectId(String id) {

    }
}
