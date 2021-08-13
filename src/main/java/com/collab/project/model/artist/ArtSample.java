package com.collab.project.model.artist;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "art_samples")
@Getter
@Setter
public class ArtSample {
    @Id
    @NonNull
    @Column(nullable = false, unique = true)
    private String artistId;
    @NonNull
    @Column(nullable = false, unique = true)
    private String url;

    private Long updatedAt;
}
