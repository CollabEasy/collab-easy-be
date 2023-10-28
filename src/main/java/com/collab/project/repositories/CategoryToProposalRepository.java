package com.collab.project.repositories;

import com.collab.project.model.proposal.CategoryToProposal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryToProposalRepository extends JpaRepository<CategoryToProposal, String> {
    List<CategoryToProposal> findByProposalId(String proposalId);

    List<CategoryToProposal> findByCategoryIdIn(List<Long> categoryIds);
}
