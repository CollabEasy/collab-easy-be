package com.collab.project.service;

import java.util.List;
import java.util.Optional;

import com.collab.project.model.inputs.ArtistSocialProspectusInput;
import com.collab.project.model.socialprospectus.ArtistSocialProspectus;

public interface ArtistSocialProspectusService {
    public ArtistSocialProspectus createSocialPlatform(ArtistSocialProspectusInput artistSocialProspectusInput);

    public Boolean updateSocialPlatform(ArtistSocialProspectusInput artistSocialProspectusInput);

    public void delete(ArtistSocialProspectusInput artistSocialProspectusInput);

    public List<ArtistSocialProspectus> getSocialProspectByArtistId(String artistId);

    public Optional<ArtistSocialProspectus> getSocialProspectusById(Long id);
}
