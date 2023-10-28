package com.collab.project.repositories;

import com.collab.project.model.proposal.ProposalInterest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProposalInterestRepository extends JpaRepository<ProposalInterest, String> {
    List<ProposalInterest> findByProposalId(String proposalId);

    ProposalInterest findByProposalId_UserId(String proposalId, String artistId);
}
