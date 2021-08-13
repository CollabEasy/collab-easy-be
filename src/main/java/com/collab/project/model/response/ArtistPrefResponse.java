package com.collab.project.model.response;

import com.collab.project.model.artist.ArtistPreference;
import lombok.Getter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class ArtistPrefResponse {
    private String artistId;

    private Map<String, String> preferences;

    public ArtistPrefResponse(List<ArtistPreference> artistPreferenceList) {
        this.preferences = new HashMap<>();
        for (ArtistPreference preference : artistPreferenceList) {
            artistId = preference.getArtistPreferenceId().getArtistId();
            preferences.put(preference.getArtistPreferenceId().getSettingName(), preference.getSettingValues());
        }
    }

    public ArtistPrefResponse(ArtistPreference artistPreference) {
        this.preferences = new HashMap<>();
        artistId = artistPreference.getArtistPreferenceId().getArtistId();
        preferences.put(artistPreference.getArtistPreferenceId().getSettingName(), artistPreference.getSettingValues());
    }

    @Override
    public String toString() {
        return "ArtistPrefResponse{" +
                "artistId='" + artistId + '\'' +
                ", preferences=" + preferences +
                '}';
    }
}
