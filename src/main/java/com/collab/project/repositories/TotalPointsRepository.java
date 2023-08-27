package com.collab.project.repositories;

import com.collab.project.model.rewards.RewardsActivity;
import com.collab.project.model.rewards.TotalPoints;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TotalPointsRepository extends JpaRepository<TotalPoints, String> {
    public Optional<TotalPoints> findByArtistId(String id);
}
