package com.collab.project.service.impl;

import com.collab.project.model.collab.CollabConversation;
import com.collab.project.repositories.CollabConversationRepository;
import com.collab.project.service.CollabConversationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.collab.project.helpers.Constants.FALLBACK_ID;

import java.util.List;

@Service
public class CollabConversationServiceImpl implements CollabConversationService {

    @Autowired
    private CollabConversationRepository collabConversationRepository;

    @Override
    public CollabConversation addComment(String collabId, String artistId, String content) {
        CollabConversation comment = new CollabConversation(FALLBACK_ID, collabId, artistId, content);
        collabConversationRepository.save(comment);
        return comment;
    }

    @Override
    public List<CollabConversation> getCommentsByCollabId(String collabId) {
        return collabConversationRepository.findByCollabId(collabId);
    }
}
