package com.collab.project.service;

import com.collab.project.model.inputs.ProposalAnswerInput;
import com.collab.project.model.inputs.ProposalInput;
import com.collab.project.model.inputs.ProposalQuestionInput;
import com.collab.project.model.proposal.*;

import java.util.List;

public interface ProposalService {
    ProposalResponse createProposal(String artistId, ProposalInput proposalInput);

    Proposal updateProposal(String artistId, String proposalId, ProposalInput proposalInput);

    ArtistProposals getArtistProposals(String artistId);

    ProposalResponse getProposal(String proposalId);

    List<ProposalResponse> getProposalByCategories(List<Long> categoryIds);

    List<ProposalQuestion> askQuestion(String artistId, String proposalId, ProposalQuestionInput questionInput);

    List<ProposalQuestion> answerQuestion(String artistId, String proposalId, ProposalAnswerInput answerInput);

    ProposalInterest updateInterest(String artistId, String proposalId, boolean interested, String message);

    List<ProposalInterest> acceptInterest(String artistId, String proposalId, List<String> acceptedArtistId);

    List<ProposalInterest> rejectInterest(String artistId, String proposalId, List<String> rejectedArtistId);

    List<ProposalQuestion> getQuestionsOnProposals(String proposalId);

    List<ProposalInterest> getAllInterests(String proposalId);
}
