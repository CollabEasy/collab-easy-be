package com.collab.project.service.impl;

import com.amazonaws.services.mq.model.UnauthorizedException;
import com.collab.project.exception.CollabRequestException;
import com.collab.project.helpers.Constants;
import com.collab.project.model.artist.Artist;
import com.collab.project.model.collab.CollabConversationReadStatus;
import com.collab.project.model.collab.CollabRequest;
import com.collab.project.model.collab.CollabRequestOutput;
import com.collab.project.model.collab.CollabRequestsStatus;
import com.collab.project.model.enums.Enums;
import com.collab.project.model.inputs.CollabRequestInput;
import com.collab.project.model.inputs.CollabRequestSearch;
import com.collab.project.model.notification.Notification;
import com.collab.project.repositories.ArtistRepository;
import com.collab.project.repositories.CollabConversationReadStatusRepository;
import com.collab.project.repositories.CollabRequestRepository;
import com.collab.project.search.SpecificationBuilder;
import com.collab.project.service.ArtistPreferencesService;
import com.collab.project.service.CollabService;
import com.collab.project.service.NotificationService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;

@Service
public class CollabServiceImpl implements CollabService {

    @Autowired
    private ArtistPreferencesService artistPreferencesService;
    
    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private CollabRequestRepository collabRequestRepository;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private CollabConversationReadStatusRepository collabConversationReadStatusRepository;

    @Override
    public CollabRequest sendRequest(String artistId, CollabRequestInput collabRequestInput) throws JsonProcessingException {
        // Add validation on receiver id and handle idempotency on requestId
        // If there is any active collab request present, there should not be a new request.
        List<String> status =  Arrays.asList(Enums.CollabStatus.PENDING.toString(),
                Enums.CollabStatus.ACTIVE.toString(), Enums.CollabStatus.REJECTED.toString());

        List<CollabRequest> collabRequestBySender = collabRequestRepository.findBySenderIdAndReceiverIdAndStatus(
                artistId,
                collabRequestInput.getReceiverId(),
                String.valueOf(Enums.CollabStatus.PENDING)
        );
        List<CollabRequest> collabRequestByReceiver = collabRequestRepository.findBySenderIdAndReceiverIdAndStatus(
                collabRequestInput.getReceiverId(),
                artistId,
                String.valueOf(Enums.CollabStatus.PENDING)
        );
        if(!collabRequestBySender.isEmpty() || !collabRequestByReceiver.isEmpty()) {
            throw new CollabRequestException("Collab Request already exists");
        }

        Artist sender = artistRepository.getOne(artistId);
        Artist receiver = artistRepository.getOne(collabRequestInput.getReceiverId());

        CollabRequest saveCollabRequest = CollabRequest.builder()
                .id(UUID.randomUUID().toString())
                .senderId(artistId).receiverId(collabRequestInput.getReceiverId())
                .collabDate(Timestamp.valueOf(collabRequestInput.getCollabDate()))
                .status(Enums.CollabStatus.PENDING.toString())
                .requestData(collabRequestInput.getRequestData())
                .createdAt(Timestamp.from(Instant.now()))
                .senderName(sender.getFirstName())
                .receiverName(receiver.getFirstName())
                .senderSlug(sender.getSlug())
                .receiverSlug(receiver.getFirstName())
                .updatedAt(Timestamp.from(Instant.now()))
                .build();

        saveCollabRequest = collabRequestRepository.save(saveCollabRequest);
        artistPreferencesService.updateArtistPreferences(artistId, new HashMap<String, Object>() {{
            put("upForCollaboration", true);
        }});
        // TODO : Uncomment this when notification service will be added
//        notificationService.addNotification(Notification.builder()
//                                                    .artistId(artistId)
//                                                    .redirectId(collabRequestInput.getReceiverId())
//                                                    .notifType("collabRequest")
//                                                    .notifRead(false)
//                                                    .notificationData(String.format("%s send you a collab request", artistId))
//                                                    .build());


        return saveCollabRequest;
    }

    @Override
    public CollabRequest updateRequest(String artistId, CollabRequest collabRequestInput) {
        Optional<CollabRequest> collabRequest = collabRequestRepository.findById(collabRequestInput.getId());
        if (collabRequest.isPresent()) {
            collabRequestInput.setUpdatedAt(Timestamp.from(Instant.now()));
            collabRequestRepository.save(collabRequestInput);
        }
        return collabRequestInput;
    }

    @Override
    public void cancelRequest(String artistId, String requestId) {
        Optional<CollabRequest> collabRequest = collabRequestRepository.findById(requestId);
        if (collabRequest.isPresent()) {
            CollabRequest request = collabRequest.get();
            if (request.getSenderId().equalsIgnoreCase(artistId)) {
                collabRequestRepository.delete(collabRequest.get());
            } else {
                throw new UnauthorizedException("You are not authorized to perform this action.");
            }
        }
    }

    @Override
    public CollabRequest completeRequest(String artistId, String completeRequestId) {
        Optional<CollabRequest> collabCompletedByReceiver = collabRequestRepository.findById(completeRequestId);
        if(collabCompletedByReceiver.isPresent()) {
            CollabRequest collabRequest = collabCompletedByReceiver.get();
            if((collabRequest.getReceiverId().equals(artistId) || collabRequest.getSenderId().equals(artistId)) && collabRequest.getStatus().equals(Enums.CollabStatus.ACTIVE.toString())) {
                collabRequest.setStatus(Enums.CollabStatus.COMPLETED.toString());
                collabRequest = collabRequestRepository.save(collabRequest);
                return collabRequest;
            } else {
                if(!collabRequest.getReceiverId().equals(artistId) && !collabRequest.getSenderId().equals(artistId)) {
                    throw new CollabRequestException(String.format("user not authorized to complete request"));
                }
                else if(!collabRequest.getStatus().equals(Enums.CollabStatus.ACTIVE.toString())) {
                    throw new CollabRequestException(String.format("collab request status is not in active state"));
                }
                throw new CollabRequestException(String.format("error while completing request"));
            }
        } else {
            throw new CollabRequestException(String.format("collab request id is not present"));
        }
    }
    @Override
    public CollabRequest rejectRequest(String artistId, String rejectRequestId) {
        Optional<CollabRequest> collabRejectedByReceiver = collabRequestRepository.findById(rejectRequestId);
        if(collabRejectedByReceiver.isPresent()) {
            CollabRequest collabRequest = collabRejectedByReceiver.get();
            if( collabRequest.getReceiverId().equals(artistId) && collabRequest.getStatus().equals(Enums.CollabStatus.PENDING.toString())) {
                collabRequest.setStatus(Enums.CollabStatus.REJECTED.toString());
                collabRequest = collabRequestRepository.save(collabRequest);
                return collabRequest;
            } else {
                if(!collabRequest.getReceiverId().equals(artistId)) {
                    throw new CollabRequestException(String.format("user not authorized to reject request"));
                }
                else if(!collabRequest.getStatus().equals(Enums.CollabStatus.PENDING.toString())) {
                    throw new CollabRequestException(String.format("collab request status is not in pending state"));
                }
                throw new CollabRequestException(String.format("error while rejecting request"));
            }
        } else {
            throw new CollabRequestException(String.format("collab request id is not present"));
        }
    }

    @Override
    public CollabRequest acceptRequest(String artistId, String acceptRequestId) {
        Optional<CollabRequest> collabAcceptedByReceiver = collabRequestRepository.findById(acceptRequestId);
        if(collabAcceptedByReceiver.isPresent()) {
            CollabRequest collabRequest = collabAcceptedByReceiver.get();
            if( collabRequest.getReceiverId().equals(artistId) && collabRequest.getStatus().equals(Enums.CollabStatus.PENDING.toString())) {
                collabRequest.setStatus(Enums.CollabStatus.ACTIVE.toString());
                collabRequest = collabRequestRepository.save(collabRequest);
                return collabRequest;
            } else {
                if(!collabRequest.getReceiverId().equals(artistId)) {
                    throw new CollabRequestException(String.format("user not authorized to accept request"));
                }
                else if(!collabRequest.getStatus().equals(Enums.CollabStatus.PENDING.toString())) {
                    throw new CollabRequestException(String.format("collab request status is not in pending state"));
                }
                throw new CollabRequestException(String.format("error while accepting request"));
            }
        } else {
            throw new CollabRequestException(String.format("collab request id is not present"));
        }
    }

    private CollabRequestOutput createOutput(List<CollabRequest> requests, String loggedInArtistId, Artist loggedInArtist) {
        requests.sort((o1, o2) -> o2.getUpdatedAt().compareTo(o1.getUpdatedAt()));
        CollabRequestOutput collabRequestOutput = new CollabRequestOutput();
        collabRequestOutput.setSent(new CollabRequestsStatus());
        collabRequestOutput.setReceived(new CollabRequestsStatus());
        for (CollabRequest request : requests) {
            Artist sender;
            Artist reciever;
            if (request.getSenderId().equals(loggedInArtistId)) {
                sender = loggedInArtist;
                reciever = artistRepository.findByArtistId(request.getReceiverId());
            } else {
                sender = artistRepository.findByArtistId(request.getSenderId());
                reciever = loggedInArtist;
            }

            request.setSenderName(sender.getFirstName());
            request.setReceiverName(reciever.getFirstName());
            request.setSenderSlug(sender.getSlug());
            request.setReceiverSlug(reciever.getSlug());
            request.setSenderProfilePicUrl(sender.getProfilePicUrl());
            request.setReceiverProfilePicUrl(reciever.getProfilePicUrl());

                    List<CollabConversationReadStatus> readStatusList =
                    collabConversationReadStatusRepository.findByCollabId(request.getId());
            boolean isNewComment = !readStatusList.isEmpty()
                    && (readStatusList.get(0).getArtistId().equals(loggedInArtistId)
                    || (readStatusList.size() > 1 && readStatusList.get(1).getArtistId().equals(loggedInArtistId)));

            request.setNewComment(isNewComment);
            CollabRequestsStatus collabRequestsStatus = null;
            if (request.getSenderId().equals(loggedInArtistId)) {
                collabRequestsStatus = collabRequestOutput.getSent();
            } else {
                collabRequestsStatus = collabRequestOutput.getReceived();
            }

            collabRequestsStatus.getAll().add(request);
            if (request.getStatus().equalsIgnoreCase(Enums.CollabStatus.ACTIVE.toString())) {
                collabRequestsStatus.getActive().add(request);
            } else if (request.getStatus().equalsIgnoreCase(Enums.CollabStatus.PENDING.toString())) {
                collabRequestsStatus.getPending().add(request);
            } else if (request.getStatus().equalsIgnoreCase(Enums.CollabStatus.REJECTED.toString())) {
                collabRequestsStatus.getRejected().add(request);
            } else if (request.getStatus().equalsIgnoreCase(Enums.CollabStatus.COMPLETED.toString())) {
                collabRequestsStatus.getCompleted().add(request);
            }
        }
        return collabRequestOutput;
    }

    @Override
    public CollabRequestOutput collabRequestsSearch(String loggedInArtistId, CollabRequestSearch collabRequestSearch) {
        SpecificationBuilder<CollabRequest> builder =
                new SpecificationBuilder();
        Artist loggedInArtist = artistRepository.findByArtistId(loggedInArtistId);
        if (collabRequestSearch.getCollabRequestId() != null) {
            Optional<CollabRequest> request = collabRequestRepository.findById(collabRequestSearch.getCollabRequestId());
            if (request.isPresent()) {
                CollabRequest collabRequest = request.get();
                if (collabRequest.getSenderId().equals(loggedInArtistId) || collabRequest.getReceiverId().equals(loggedInArtistId)) {
                    List<CollabRequest> result = new ArrayList<>();
                    result.add(collabRequest);
                    return createOutput(result, loggedInArtistId, loggedInArtist);
                }
            }
        }


        builder.with("senderId", ":", loggedInArtistId);
        if (collabRequestSearch.getOtherUserId() != null) {
            builder.with("receiverId", ":", collabRequestSearch.getOtherUserId());
        }
        if (!Strings.isNullOrEmpty(collabRequestSearch.getStatus())) {
            builder.with("status", ":", collabRequestSearch.getStatus());
        }

        Specification<CollabRequest> specification = builder.build();
        List<CollabRequest> collabRequests = collabRequestRepository.findAll(specification);
        updateCollabRequestStatus(collabRequests);

        builder = new SpecificationBuilder();
        builder.with("receiverId", ":", loggedInArtistId);
        if (collabRequestSearch.getOtherUserId() != null) {
            builder.with("senderId", ":", collabRequestSearch.getOtherUserId());
        }
        if (!Strings.isNullOrEmpty(collabRequestSearch.getStatus())) {
            builder.with("status", ":", collabRequestSearch.getStatus());
        }
        updateCollabRequestStatus(collabRequests);
        specification = builder.build();
        collabRequests.addAll(collabRequestRepository.findAll(specification));

        return createOutput(collabRequests, loggedInArtistId, loggedInArtist);
    }

    private void updateCollabRequestStatus(List<CollabRequest> collabRequests) {
        for (CollabRequest request : collabRequests) {
            if (request.getCollabDate().before(Timestamp.from(Instant.now())) &&
                    !request.getStatus().equalsIgnoreCase(String.valueOf(Enums.CollabStatus.COMPLETED)) &&
                            !request.getStatus().equalsIgnoreCase(String.valueOf(Enums.CollabStatus.EXPIRED))) {
                if (request.getStatus().equalsIgnoreCase(Enums.CollabStatus.ACTIVE.toString())) {
                    request.setStatus(String.valueOf(Enums.CollabStatus.COMPLETED));
                } else {
                    request.setStatus(String.valueOf(Enums.CollabStatus.EXPIRED));
                }
                collabRequestRepository.save(request);
            }
        }
    }


}
