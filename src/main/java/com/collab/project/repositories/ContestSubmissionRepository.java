package com.collab.project.repositories;

import com.collab.project.model.contest.ContestSubmission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ContestSubmissionRepository extends JpaRepository<ContestSubmission, Long> {

    public Optional<ContestSubmission> findById(Long id);

    public Optional<ContestSubmission> findByArtistId(String artistId);
    public List<ContestSubmission> findByContestSlug(String slug);

    @Query(value = "SELECT * FROM contest_submissions where artist_id = (?2) AND contest_slug = (?1) ", nativeQuery = true)
    public ContestSubmission findByContestSlugAndArtistId(String slug, String artistId);
}
