package com.collab.project.model.artist;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "artist_preferences")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class ArtistPreference {
    private static final long serialVersionUID = 1L;
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @NotNull
    @Column(name = "id")
    private Long id;

    @EmbeddedId
    private ArtistPreferenceId artistPreferenceId;

    @Column(name = "setting_values")
    private String settingValues;

    public ArtistPreference(String artistId, String settingName, String settingValues)  {
        this.artistPreferenceId = new ArtistPreferenceId(artistId, settingName);
        this.settingValues = settingValues;
    }

    @Override
    public String toString() {
        return "ArtistPreference{" +
                "id=" + id +
                ", artistPreferenceId=" + artistPreferenceId +
                ", settingValues='" + settingValues + '\'' +
                '}';
    }
}
