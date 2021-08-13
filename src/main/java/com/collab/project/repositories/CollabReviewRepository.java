package com.collab.project.repositories;

import com.collab.project.model.collab.CollabReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CollabReviewRepository extends JpaRepository<CollabReview, String> {
}
