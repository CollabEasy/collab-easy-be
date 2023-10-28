package com.collab.project.repositories;

import com.collab.project.model.proposal.ProposalQuestion;
import com.collab.project.model.socialprospectus.ArtistSocialProspectus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProposalQuestionsRepository extends JpaRepository<ProposalQuestion, Long> {
    List<ProposalQuestion> findByProposalId(String proposalId);

    @Query(value = "SELECT * FROM proposal_questions where proposal_id = (?1) AND question_id = (?2) ", nativeQuery = true)
    public ProposalQuestion findByProposalIdAndQuestionId(String proposalId, String questionId);
}
