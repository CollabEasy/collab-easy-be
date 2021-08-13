package com.collab.project.model.artist;

import com.collab.project.helpers.SerdeHelper;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Map;

@Entity
@Table(name = "artist-preferences")
@Getter
@Setter
@AllArgsConstructor
public class ArtistPreference {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @NotNull
    private String artistId;

    @NotNull
    private String settingName;

    private String settingValues;

    public ArtistPreference(String artistId, String settingName, String settingValues)  {
        this.artistId = artistId;
        this.settingName = settingName;
        this.settingValues = settingValues;
    }
}
