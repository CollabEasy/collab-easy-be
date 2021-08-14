package com.collab.project.model.artist;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Map;

@Entity
@Table(name = "artist-preferences")
@Getter
@Setter
public class ArtistPreference {
    private String artistId;

    private String settingName;

    private Map<String, Boolean> settingValues;
}
