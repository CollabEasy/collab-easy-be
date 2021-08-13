package com.collab.project.service.impl;

import com.collab.project.exception.RecordNotFoundException;
import com.collab.project.model.artist.ArtistPreference;
import com.collab.project.repositories.ArtistPreferenceRepository;
import com.collab.project.service.ArtistPreferencesService;
import io.jsonwebtoken.lang.Collections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ArtistPreferencesImpl implements ArtistPreferencesService {

    @Autowired
    ArtistPreferenceRepository artistPreferenceRepository;

    @Override
    public void updateArtistPreferences(ArtistPreference artistPreferences) {
        artistPreferenceRepository.save(artistPreferences);
    }

    @Override
    public void updateArtistPreferences(String artistId, String settingName, Object settingValue) {
        artistPreferenceRepository.updateArtistPreferences(artistId, settingName, settingValue);
    }

    @Override
    public List<ArtistPreference> getArtistPreferences(String artistId) throws RecordNotFoundException {
        List<ArtistPreference> existing = artistPreferenceRepository.findPreferencesByArtistId(artistId);
        if (Collections.isEmpty(existing)) throw new RecordNotFoundException("No artist preferences exist for given artist ID");
        return existing;
    }

    @Override
    public ArtistPreference getArtistPreferences(String artistId, String settingName) throws RecordNotFoundException {
        ArtistPreference existing = artistPreferenceRepository.findByArtistIdAndSettingName(artistId, settingName);
        if (existing == null) throw new RecordNotFoundException("No artist preferences exist for given artist ID and setting name");
        return existing;
    }
}
