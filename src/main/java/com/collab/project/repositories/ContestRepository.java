package com.collab.project.repositories;

import com.collab.project.model.contest.Contest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ContestRepository extends JpaRepository<Contest, Long> {
    public Optional<Contest> findById(Long id);

    public Optional<Contest> findByContestSlug(String slug);
}