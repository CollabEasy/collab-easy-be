package com.collab.project.repositories;

import com.collab.project.model.collab.CollabConversationReadStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CollabConversationReadStatusRepository extends JpaRepository<CollabConversationReadStatus, Long> {

    public List<CollabConversationReadStatus> findByCollabId(String collabId);
}
