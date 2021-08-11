package com.collab.project.service.impl;

import com.collab.project.exception.RecordNotFoundException;
import com.collab.project.model.artist.ArtistPreference;
import com.collab.project.repositories.ArtistPreferenceRepository;
import com.collab.project.service.ArtistPreferencesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ArtistPreferencesImpl implements ArtistPreferencesService {

    @Autowired
    ArtistPreferenceRepository artistPreferenceRepository;

    @Override
    public void updateArtistPreferences(String artistId, ArtistPreference artistPreferences) {
        artistPreferenceRepository.save(artistPreferences);
    }

    @Override
    public ArtistPreference getArtistPreferences(String artistId) throws RecordNotFoundException {
        Optional<ArtistPreference> existing = artistPreferenceRepository.findById(artistId);
        if (!existing.isPresent()) throw new RecordNotFoundException("No artist preferences exist for given ID");
        return existing.get();
    }
}
