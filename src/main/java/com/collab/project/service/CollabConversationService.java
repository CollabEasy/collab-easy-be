package com.collab.project.service;

import com.collab.project.model.collab.CollabConversation;
import com.collab.project.model.scratchpad.Scratchpad;
import java.util.List;

public interface CollabConversationService {
    public CollabConversation addComment(String collabId, String artistId, String content);
    public List<CollabConversation> getCommentsByCollabId(String collabId);
}
