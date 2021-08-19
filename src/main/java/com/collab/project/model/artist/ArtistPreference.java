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

@Entity
@Table(name = "artist_preferences")
@Getter
@Setter
public class ArtistPreference {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    Long id;
    @Column(name="artist_id")
    private String artistId;
    @Column(name = "setting_name")
    private String settingName;

    @Column(name = "setting_values")
    private String settingValues;
}
