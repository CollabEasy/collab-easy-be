package com.collab.project.model.artist;

import lombok.Getter;
import lombok.NonNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "artist-categories")
@Getter
public class ArtistCategory {
    @NonNull
    @Column(nullable = false, unique = true)
    private String artistId;
    @NonNull
    @Column(nullable = false, unique = true)
    private String artId;
}
