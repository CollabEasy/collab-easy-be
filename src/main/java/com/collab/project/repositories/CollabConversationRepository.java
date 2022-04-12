package com.collab.project.repositories;

import com.collab.project.model.collab.CollabConversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CollabConversationRepository extends JpaRepository<CollabConversation, Long> {
    public List<CollabConversation> findByCollabId(String collabId);
}
