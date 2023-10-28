package com.collab.project.repositories;

import com.collab.project.model.proposal.Proposal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProposalRepository extends JpaRepository<Proposal, Integer> {
    Proposal findByProposalId(String proposalId);

    List<Proposal> findByCreatedBy(String artistId);

    List<Proposal> findByProposalIdIn(List<String> proposalIds);
}
