package com.collab.project.repositories;

import com.collab.project.model.proposal.ProposalInterest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProposalInterestRepository extends JpaRepository<ProposalInterest, String> {
    List<ProposalInterest> findByProposalId(String proposalId);

    ProposalInterest findByProposalId_UserId(String proposalId, String artistId);
}
