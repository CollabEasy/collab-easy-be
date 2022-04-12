package com.collab.project.service;

import com.collab.project.model.collab.CollabConversation;
import com.collab.project.model.scratchpad.Scratchpad;
import java.util.List;

public interface CollabConversationService {
    public CollabConversation addComment(String artistId, String collabId, String content);
    public List<CollabConversation> getCommentsByCollabId(String collabId);
    public void markCommentRead(String artistId, String collabId);
}
