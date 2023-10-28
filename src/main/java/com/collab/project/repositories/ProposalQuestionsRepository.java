package com.collab.project.repositories;

import com.collab.project.model.proposal.ProposalQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProposalQuestionsRepository extends JpaRepository<ProposalQuestion, Long> {
    List<ProposalQuestion> findByProposalId(String proposalId);

    ProposalQuestion findByProposalId_QuestionId(String proposalId, String questionId);
}
