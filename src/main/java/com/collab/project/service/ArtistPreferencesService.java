package com.collab.project.service;

import com.collab.project.exception.RecordNotFoundException;
import com.collab.project.model.artist.ArtistPreference;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.List;
import java.util.Map;

public interface ArtistPreferencesService {

    public void updateArtistPreferences(String artistId, Map<String, Object> artistPreferences) throws JsonProcessingException;

    public void updateArtistPreferences(ArtistPreference artistPreference);

    public List<ArtistPreference> getArtistPreferences(String artistId) throws RecordNotFoundException;

    public ArtistPreference getArtistPreferences(String artistId, String settingName) throws RecordNotFoundException;
}
