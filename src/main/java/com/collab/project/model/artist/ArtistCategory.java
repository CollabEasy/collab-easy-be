package com.collab.project.model.artist;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "artist_categories")
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ArtistCategory {
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NonNull
    @Column(name = "artist_id", nullable = false)
    private String artistId;

    @NonNull
    @Column(name = "art_id", nullable = false)
    private Long artId;
}
