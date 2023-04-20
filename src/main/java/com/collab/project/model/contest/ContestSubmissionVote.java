package com.collab.project.model.contest;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.*;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

@Entity
@Table(name = "contest_submission_vote")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class ContestSubmissionVote {
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "contest_slug")
    private String contestSlug;

    @NotNull
    @Column(name = "submission_id")
    private Long submissionId;

    @NonNull
    @Column(name = "artist_id", nullable = false)
    private String artistId;

    @Column(name = "vote")
    private Boolean vote;

    @Column(name="created_at", updatable = false, insertable = false, nullable = false)
    private Timestamp createdAt;

    @Column(name="updated_at", updatable = false, insertable = false, nullable = false)
    private Timestamp updatedAt;

}
