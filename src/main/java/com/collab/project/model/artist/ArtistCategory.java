package com.collab.project.model.artist;

import lombok.Getter;
import lombok.NonNull;

import javax.persistence.*;

@Entity
@Table(name = "artist-categories")
@Getter
public class ArtistCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @NonNull
    @Column(nullable = false, unique = true)
    private String artistId;
    @NonNull
    @Column(nullable = false, unique = true)
    private String artId;
}
