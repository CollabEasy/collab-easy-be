package com.collab.project.repositories;

import com.collab.project.model.rewards.RewardsActivity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RewardsActivityRepository extends JpaRepository<RewardsActivity, String> {
    public Optional<List<RewardsActivity>> findByArtistId(Long id);
}
