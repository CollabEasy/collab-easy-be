package com.collab.project.repositories;

import com.collab.project.model.proposal.ProposalQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProposalQuestionsRepository extends JpaRepository<ProposalQuestion, String> {
    List<ProposalQuestion> findByProposalId(String proposalId);

    ProposalQuestion findByProposalId_QuestionId(String proposalId, String questionId);
}
