package com.collab.project.model.contest;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.*;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;


@Entity
@Table(name = "contest_submissions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class ContestSubmission {
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "contest_slug")
    private String contestSlug;

    @NonNull
    @Column(name = "artist_id", nullable = false)
    private String artistId;

    @NonNull
    @Column(name = "artwork_url", nullable = false)
    private String artworkUrl;

    @NonNull
    @Column(name = "artwork_thumb_url", nullable = false)
    private String artworkThumbnailUrl;

    @Column(name = "description")
    private String description;

    @Column(name="created_at", updatable = false, insertable = false, nullable = false)
    private Timestamp createdAt;

    @Column(name="updated_at", updatable = false, insertable = false, nullable = false)
    private Timestamp updatedAt;

}
