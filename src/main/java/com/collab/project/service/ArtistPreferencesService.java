package com.collab.project.service;

import com.collab.project.exception.RecordNotFoundException;
import com.collab.project.model.artist.ArtistPreference;

import java.util.Optional;

public interface ArtistPreferencesService {

    public void updateArtistPreferences(String artistId, ArtistPreference artistPreferences);

    public ArtistPreference getArtistPreferences(String artistId) throws RecordNotFoundException;
}
