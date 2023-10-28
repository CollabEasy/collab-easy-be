package com.collab.project.model.proposal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "category_to_proposal")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryToProposal {
    @NotNull
    @Column(name = "category_id")
    private Long categoryId;

    @NotNull
    @Column(name = "proposal_id")
    private String proposalId;
}
