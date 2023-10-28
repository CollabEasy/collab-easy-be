package com.collab.project.model.proposal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

@Entity
@Table(name = "proposal_interests")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProposalInterest {

    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

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