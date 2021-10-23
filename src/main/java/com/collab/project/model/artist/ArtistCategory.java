package com.collab.project.model.artist;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

@Entity
@Table(name = "artist_categories")
@Getter
@Setter
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

    @NonNull
    @Column(name = "enabled", nullable = false)
    private Boolean enabled;
}
