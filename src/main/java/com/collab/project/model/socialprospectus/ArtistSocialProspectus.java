package com.collab.project.model.socialprospectus;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.*;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.TypeDef;

@Entity
@Table(name = "artist_social_prospectus")
@Getter
@Setter
@NoArgsConstructor
@Builder
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
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

    @Column(name = "up_for_collab")
    private String upForCollab;

    public ArtistSocialProspectus(Long id, @org.jetbrains.annotations.NotNull String artistId, @org.jetbrains.annotations.NotNull Long socialPlatformId, String handle, String description, String upForCollab) {
        this.id = id;
        this.artistId = artistId;
        this.socialPlatformId = socialPlatformId;
        this.handle = handle;
        this.description = description;
        this.upForCollab = upForCollab;
    }
}
