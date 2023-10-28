package com.collab.project.model.proposal;

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
}
