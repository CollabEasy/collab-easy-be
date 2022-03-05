package com.collab.project.model.socialprospectus;

import lombok.*;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "artist_social_prospectus")
@Getter
@Setter
public class ArtistSocialProspectus {
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
    @Column(name = "social_platform_id", nullable = false)
    private Long socialPlatformId;

    @Column(name = "handle")
    private String handle;

    @Column(name = "description")
    private String description;

    public ArtistSocialProspectus(Long id, String artistId, Long socialPlatformId, String handle, String description) {
        this.id = id;
        this.artistId = artistId;
        this.socialPlatformId = socialPlatformId;
        this.handle = handle;
        this.description = description;
    }
}
