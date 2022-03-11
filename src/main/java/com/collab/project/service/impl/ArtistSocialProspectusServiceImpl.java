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
public class ArtistSocialProspectusServiceImpl implements ArtistSocialProspectusService {
    @Autowired
    private ArtistSocialProspectusRepository artistSocialProspectusRepository;

    @Autowired
    private ArtistRepository artistRepository;

    public ArtistSocialProspectus createSocialPlatform(ArtistSocialProspectusInput artistSocialProspectusInput) {
        ArtistSocialProspectus prospectus = new ArtistSocialProspectus(FALLBACK_ID, AuthUtils.getArtistId(),
                artistSocialProspectusInput.getSocialPlatformId(), artistSocialProspectusInput.getHandle(),
                artistSocialProspectusInput.getDescription());

        return artistSocialProspectusRepository.save(prospectus);
    }

    public Boolean updateSocialPlatform(ArtistSocialProspectusInput artistSocialProspectusInput) {
        return true;
    }

    public void delete(ArtistSocialProspectusInput artistSocialProspectusInput) {

    }

    public List<ArtistSocialProspectus> getSocialProspectByArtistId(String artistId) {
        List<ArtistSocialProspectus> prospectus = artistSocialProspectusRepository.findByArtistId(artistId);
        return prospectus;
    }

    public Optional<ArtistSocialProspectus> getSocialProspectusById(Long id) {
        Optional<ArtistSocialProspectus> lola =  artistSocialProspectusRepository.findById(id);
        return lola;
    }
}
