package com.collab.project.model.proposal;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

@Entity
@Table(name = "proposal_questions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProposalQuestion {
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "proposal_id")
    private String proposalId;

    @NotNull
    @Column(name = "question_id")
    private String questionId;

    @Column(name = "question")
    private String question;

    @Column(name = "answer")
    private String answer;

    @Column(name = "asked_by")
    private String askedBy;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "updated_at")
    private Timestamp updatedAt;

    @Transient
    @JsonProperty
    private String askedByFirstName;

    @Transient
    @JsonProperty
    private String askedByLastName;

    @Transient
    @JsonProperty
    private String askedByProfilePic;

    @Transient
    @JsonProperty
    private String askedBySlug;
}
