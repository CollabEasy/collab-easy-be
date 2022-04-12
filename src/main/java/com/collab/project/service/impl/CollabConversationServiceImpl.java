package com.collab.project.service.impl;

import com.amazonaws.services.mq.model.BadRequestException;
import com.amazonaws.services.mq.model.UnauthorizedException;
import com.collab.project.exception.RecordNotFoundException;
import com.collab.project.model.collab.CollabConversation;
import com.collab.project.model.collab.CollabRequest;
import com.collab.project.model.enums.Enums;
import com.collab.project.repositories.CollabConversationRepository;
import com.collab.project.repositories.CollabRequestRepository;
import com.collab.project.service.CollabConversationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.collab.project.helpers.Constants.FALLBACK_ID;

import java.util.List;
import java.util.Optional;

@Service
public class CollabConversationServiceImpl implements CollabConversationService {

    @Autowired
    private CollabConversationRepository collabConversationRepository;

    @Autowired
    private CollabRequestRepository collabRequestRepository;

    @Override
    public CollabConversation addComment(String artistId, String collabId, String content) {
        Optional<CollabRequest> collabRequestOptional = collabRequestRepository.findById(collabId);
        if (!collabRequestOptional.isPresent()) {
            throw new RecordNotFoundException("No such collab Id exists.");
        }

        CollabRequest collabRequest = collabRequestOptional.get();
        if (!collabRequest.getStatus().equals(Enums.CollabStatus.PENDING.name())
                && !collabRequest.getStatus().equals(Enums.CollabStatus.ACTIVE.name())) {
            throw new RecordNotFoundException("No such collab id exists with valid status.");
        }

        if (!collabRequest.getSenderId().equals(artistId) && !collabRequest.getReceiverId().equals(artistId)) {
            throw new UnauthorizedException("You are not authorized to access this.");
        }

        CollabConversation comment = new CollabConversation(FALLBACK_ID, collabId, artistId, content);
        collabConversationRepository.save(comment);
        return comment;
    }

    @Override
    public List<CollabConversation> getCommentsByCollabId(String collabId) {
        return collabConversationRepository.findByCollabId(collabId);
    }
}
