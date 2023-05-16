package com.collab.project.repositories;

import com.collab.project.model.contest.ContestSubmissionVote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ContestSubmissionVoteRepository extends JpaRepository<ContestSubmissionVote, Long> {
    public Optional<ContestSubmissionVote> findById(Long id);
    public Optional<ContestSubmissionVote> findByArtistId(String artistId);
    public List<ContestSubmissionVote> findByContestSlug(String contestSlug);

    @Query(value = "SELECT * FROM contest_submission_vote where id = (?1) AND contest_slug = (?2) ", nativeQuery = true)
    public Optional<ContestSubmissionVote> findByIdAndContestSlug(Long id, String contestSlug);

    @Query(value = "SELECT * FROM contest_submission_vote where artist_id = (?1) AND contest_slug = (?2) AND vote = 1", nativeQuery = true)
    public List<ContestSubmissionVote> findByArtistIdAndContestSlug(String artistSd, String contestSlug);
}
