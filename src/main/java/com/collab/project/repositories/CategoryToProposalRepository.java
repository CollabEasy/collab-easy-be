package com.collab.project.repositories;

import com.collab.project.model.proposal.CategoryToProposal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryToProposalRepository extends JpaRepository<CategoryToProposal, String> {
    List<CategoryToProposal> findByProposalId(String proposalId);

    List<CategoryToProposal> findByCategoryIdIn(List<Long> categoryIds);
}
