package com.collab.project.model.proposal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

@Entity
@Table(name = "proposal_interests")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProposalInterest {
    @NotNull
    @Column(name = "proposal_id")
    private String proposalId;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "accepted")
    private boolean accepted;

    @Column(name = "message")
    private String message;

    @Column(name = "created_at")
    private Timestamp createdAt;

}
