package com.collab.project.service.impl;

import com.collab.project.model.contest.ContestSubmissionVote;
import com.collab.project.repositories.ContestSubmissionVoteRepository;
import com.collab.project.service.ContestSubmissionVoteService;
import com.collab.project.util.AuthUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static com.collab.project.helpers.Constants.FALLBACK_ID;

@Service
public class ContestSubmissionVoteServiceImpl implements ContestSubmissionVoteService {


    @Autowired
    private ContestSubmissionVoteRepository contestSubmissionVoteRepository;

    @Override
    public ContestSubmissionVote updateContestSubmissionVote(Long submissionId, String contestSlug) {
        Optional<ContestSubmissionVote> existingVote = contestSubmissionVoteRepository.findByIdAndContestSlug(submissionId, contestSlug);
        if (!existingVote.isPresent()) {
            ContestSubmissionVote newVote = new ContestSubmissionVote();
            newVote.setId(FALLBACK_ID);
            newVote.setContestSlug(contestSlug);
            newVote.setSubmissionId(submissionId);
            newVote.setArtistId(AuthUtils.getArtistId());
            newVote.setVote(true);
            newVote.setCreatedAt(Timestamp.from(Instant.now()));
            newVote.setUpdatedAt(Timestamp.from(Instant.now()));
            contestSubmissionVoteRepository.save(newVote);
            return newVote;
        }

        existingVote.get().setVote(!existingVote.get().getVote());
        existingVote.get().setUpdatedAt(Timestamp.from(Instant.now()));
        contestSubmissionVoteRepository.save(existingVote.get());
        return existingVote.get();
    }

    public List<ContestSubmissionVote> getContestSubmissionVoteByArtist(String contestSlug, String artistId) {
        List<ContestSubmissionVote> votes = contestSubmissionVoteRepository.findByArtistIdAndContestSlug(artistId, contestSlug);
        return votes;
    }
}
