package com.collab.project.service.impl;

import com.collab.project.model.artist.ArtistPreference;
import com.collab.project.model.inputs.ArtistSocialProspectusInput;
import com.collab.project.model.socialprospectus.ArtistSocialProspectus;
import com.collab.project.service.ArtistPreferencesService;
import com.collab.project.exception.RecordNotFoundException;
import com.collab.project.helpers.SerdeHelper;
import com.collab.project.model.artist.ArtistPreference;
import com.collab.project.model.artist.ArtistPreferenceId;
import com.collab.project.repositories.ArtistPreferenceRepository;
import com.collab.project.service.ArtistPreferencesService;
import com.collab.project.service.ArtistSocialProspectusService;
import com.collab.project.util.AuthUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.jsonwebtoken.lang.Collections;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.collab.project.helpers.Constants.FALLBACK_ID;

@Service
@Slf4j
public class ArtistPreferencesImpl implements ArtistPreferencesService {

    @Autowired
    ArtistSocialProspectusService artistSocialProspectusService;

    @Autowired
    ArtistPreferenceRepository artistPreferenceRepository;

    @Override
    public void updateArtistPreferences(String artistId, Map<String, Object> artistPreferences)
        throws JsonProcessingException {
        List<ArtistPreference> artistPreferenceList = new ArrayList<>();
        for (String prefName : artistPreferences.keySet()) {
            Object prefValueObj = artistPreferences.get(prefName);
            String prefValue = SerdeHelper.getJsonStringFromObject(prefValueObj);
            ArtistPreference preference = new ArtistPreference(artistId, prefName, prefValue);
            preference.setId(FALLBACK_ID);
            artistPreferenceList.add(preference);

            if (prefName.equals("upForCollaboration")) {
                List<ArtistSocialProspectus> prospectuses = artistSocialProspectusService.getSocialProspectByArtistId(AuthUtils.getArtistId());
                for (ArtistSocialProspectus prospectus : prospectuses) {
                    ArtistSocialProspectusInput input = new ArtistSocialProspectusInput();
                    input.setArtistId(prospectus.getArtistId());
                    input.setSocialPlatformId(prospectus.getSocialPlatformId());
                    input.setDescription(prospectus.getDescription());
                    input.setUpForCollab(prefValue);
                    input.setHandle(prospectus.getHandle());
                    artistSocialProspectusService.addArtistSocialProspectus(input);
                }
            }
        }
        artistPreferenceRepository.saveAll(artistPreferenceList);
        log.info("Artist preference update for aId {}", artistId);
    }

    @Override
    public void updateArtistPreferences(ArtistPreference artistPreference) {
        artistPreference.setId(FALLBACK_ID);
        artistPreferenceRepository.save(artistPreference);
    }

    @Override
    public List<ArtistPreference> getArtistPreferences(String artistId)
        throws RecordNotFoundException {
        List<ArtistPreference> existing = artistPreferenceRepository
            .findByArtistPreferenceId_ArtistId(artistId);
        if (Collections.isEmpty(existing)) {
            throw new RecordNotFoundException("No artist preferences exist for given artist ID");
        }
        return existing;
    }

    @Override
    public ArtistPreference getArtistPreferences(String artistId, String settingName)
        throws RecordNotFoundException {
        Optional<ArtistPreference> existing = artistPreferenceRepository
            .findById(new ArtistPreferenceId(artistId, settingName));
        if (!existing.isPresent()) {
            throw new RecordNotFoundException(
                "No artist preferences exist for given artist ID and setting name");
        }
        return existing.get();
    }
}
