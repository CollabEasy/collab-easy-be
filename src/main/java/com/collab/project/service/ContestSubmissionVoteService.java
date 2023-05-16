package com.collab.project.service;

import com.collab.project.model.contest.ContestSubmissionVote;

import java.util.List;

public interface ContestSubmissionVoteService {
    public ContestSubmissionVote updateContestSubmissionVote(String artistId, Long submissionId, String contestSlug);

    public List<ContestSubmissionVote> getContestSubmissionVoteByArtist(String contestSlug, String artistId);
}