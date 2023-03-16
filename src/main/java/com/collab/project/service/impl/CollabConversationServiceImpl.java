package com.collab.project.service.impl;

import com.amazonaws.services.mq.model.BadRequestException;
import com.amazonaws.services.mq.model.UnauthorizedException;
import com.collab.project.exception.RecordNotFoundException;
import com.collab.project.model.collab.CollabConversation;
import com.collab.project.model.collab.CollabConversationReadStatus;
import com.collab.project.model.collab.CollabRequest;
import com.collab.project.model.enums.Enums;
import com.collab.project.repositories.CollabConversationReadStatusRepository;
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

    @Autowired
    private CollabConversationReadStatusRepository collabConversationReadStatusRepository;

    @Override
    public void markCommentRead(String artistId, String collabId) {
        List<CollabConversationReadStatus> statuses = collabConversationReadStatusRepository.findByCollabId(collabId);
        for (CollabConversationReadStatus status : statuses) {
            if (status.getArtistId().equals(artistId)) {
                collabConversationReadStatusRepository.delete(status);
                break;
            }
        }
    }

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

        String otherUserId = artistId.equals(collabRequest.getReceiverId()) ?
                collabRequest.getSenderId() : collabRequest.getReceiverId();

        CollabConversation comment = new CollabConversation(FALLBACK_ID, collabId, artistId, content);
        collabConversationRepository.save(comment);
        List<CollabConversationReadStatus> statuses = collabConversationReadStatusRepository.findByCollabId(collabId);
        boolean exists = false;
        for (CollabConversationReadStatus status : statuses) {
            if (status.getArtistId().equals(otherUserId)) {
                exists = true;
                break;
            }
        }

        if (!exists) {
            collabConversationReadStatusRepository.save(new CollabConversationReadStatus(FALLBACK_ID, collabId, otherUserId));
        }
        return comment;
    }

    @Override
    public List<CollabConversation> getCommentsByCollabId(String userId, String collabId) {
        Optional<CollabRequest> collab = collabRequestRepository.findById(collabId);
        if (!collab.isPresent() || (!collab.get().getReceiverId().equals(userId) && !collab.get().getSenderId().equals(userId))) {
            throw new RecordNotFoundException("No such collab session exists for you.");
        }
        return collabConversationRepository.findByCollabId(collabId);
    }
}
