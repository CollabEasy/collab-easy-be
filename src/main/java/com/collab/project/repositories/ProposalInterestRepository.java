package com.collab.project.repositories;

import com.collab.project.model.proposal.ProposalInterest;
import com.collab.project.model.proposal.ProposalQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProposalInterestRepository extends JpaRepository<ProposalInterest, Long> {
    List<ProposalInterest> findByProposalId(String proposalId);

    @Query(value = "SELECT * FROM proposal_interests where proposal_id = (?1) AND user_id = (?2) ", nativeQuery = true)
    public ProposalInterest findByProposalIdAndArtistId(String proposalId, String artistId);

    @Query(value = "SELECT * FROM proposal_interests where user_id = (?1) ", nativeQuery = true)
    public List<ProposalInterest> findByArtistId(String artistId);
}
