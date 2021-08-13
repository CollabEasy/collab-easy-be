package com.collab.project.service;

import com.collab.project.exception.RecordNotFoundException;
import com.collab.project.model.artist.ArtistPreference;

import java.util.List;
import java.util.Optional;

public interface ArtistPreferencesService {

    public void updateArtistPreferences(ArtistPreference artistPreferences);

    public void updateArtistPreferences(String artistId, String settingName, Object settingValue);

    public List<ArtistPreference> getArtistPreferences(String artistId) throws RecordNotFoundException;

    public ArtistPreference getArtistPreferences(String artistId, String settingName) throws RecordNotFoundException;
}
