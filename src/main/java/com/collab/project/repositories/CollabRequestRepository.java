package com.collab.project.repositories;

import com.collab.project.model.collab.CollabRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CollabRequestRepository extends JpaRepository<CollabRequest, String> {
}
