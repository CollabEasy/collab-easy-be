package com.collab.project.service;

import com.collab.project.model.artist.ArtistPreference;

public interface ArtistPreferencesService {

    public void updateArtistPreferences(ArtistPreference artistPreferences) throws Exception;

    public ArtistPreference getArtistPreferences(String artistId);
}
