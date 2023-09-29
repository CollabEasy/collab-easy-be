package com.collab.project.service;

import java.util.List;
import java.util.Optional;

import com.collab.project.model.inputs.ArtistSocialProspectusInput;
import com.collab.project.model.socialprospectus.ArtistSocialProspectus;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface ArtistSocialProspectusService {

    public ArtistSocialProspectus addArtistSocialProspectus(ArtistSocialProspectusInput artistSocialProspectusInput) throws JsonProcessingException;
    public List<ArtistSocialProspectus> getSocialProspectByArtistId(String artistId);
    public boolean deleteSocialProspectus(String artistId, Long platformId);
}
