package com.collab.project.model.artist;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

@Entity
@Table(name = "artist_samples")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ArtSample {

    @Id
    String id;

    @NonNull
    @Column(nullable = false)
    private String artistId;

    @NonNull
    @Column(nullable = false, unique = true)
    private String originalUrl;

    @NonNull
    @Column(nullable = false, unique = true)
    private String thumbnailUrl;

    @NonNull
    @Column(nullable = false)
    private String fileType;

    private Timestamp createdAt;
}
