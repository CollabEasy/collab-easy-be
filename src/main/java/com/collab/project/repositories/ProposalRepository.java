package com.collab.project.repositories;

import com.collab.project.model.proposal.Proposal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProposalRepository extends JpaRepository<Proposal, Integer> {
    Proposal findByProposalId(String proposalId);

    List<Proposal> findByCreatedBy(String artistId);

    List<Proposal> findByProposalIdIn(List<String> proposalIds);
}
