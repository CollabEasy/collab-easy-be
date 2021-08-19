package com.collab.project.model.artist;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Getter;
import lombok.NonNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "artist_categories")
@Getter
public class ArtistCategory {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    Long id;

    @NonNull
    @Column(name = "artist_id", nullable = false, unique = true)
    private String artistId;
    @NonNull
    @Column(name = "art_id", nullable = false, unique = true)
    private String artId;
}
