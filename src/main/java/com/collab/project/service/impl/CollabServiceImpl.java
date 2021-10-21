package com.collab.project.service.impl;

import com.collab.project.exception.CollabRequestException;
import com.collab.project.exception.RecordNotFoundException;
import com.collab.project.helpers.Constants;
import com.collab.project.model.artist.Artist;
import com.collab.project.model.collab.CollabRequest;
import com.collab.project.model.enums.Enums;
import com.collab.project.model.inputs.CollabRequestInput;
import com.collab.project.model.inputs.CollabRequestSearch;
import com.collab.project.model.notification.Notification;
import com.collab.project.repositories.ArtistRepository;
import com.collab.project.repositories.CollabRequestRepository;
import com.collab.project.search.SpecificationBuilder;
import com.collab.project.service.CollabService;
import com.collab.project.service.NotificationService;
import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CollabServiceImpl implements CollabService {

    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private CollabRequestRepository collabRequestRepository;

    @Autowired
    private NotificationService notificationService;

    @Override
    public CollabRequest sendRequest(String artistId, CollabRequestInput collabRequestInput) {
        //TODO: Add validation on receiver id and handle idempotency on requestId
        List<String> status =  Arrays.asList( Enums.CollabStatus.PENDING.toString(),
                Enums.CollabStatus.ACTIVE.toString(), Enums.CollabStatus.REJECTED.toString());

        List<CollabRequest> collabRequestBySender = collabRequestRepository
                                                            .findBySenderIdAndReceiverIdAndStatusIn(artistId, collabRequestInput.getReceiverId(), status);
        List<CollabRequest> collabRequestByReceiver = collabRequestRepository
                                                              .findBySenderIdAndReceiverIdAndStatusIn(collabRequestInput.getReceiverId(), artistId, status);
        if(!collabRequestBySender.isEmpty() || !collabRequestByReceiver.isEmpty()) {
            throw new CollabRequestException("Collabaration already happened or Rejected");
        }

        String collabId = UUID.randomUUID().toString();
        CollabRequest saveCollabRequest = CollabRequest.builder()
                .id(collabId)
                .senderId(artistId).receiverId(collabRequestInput.getReceiverId())
                .collabDate(Timestamp.valueOf(collabRequestInput.getCollabDate()))
                .status(Enums.CollabStatus.PENDING.toString())
                .requestData(collabRequestInput.getRequestData())
                .build();
        saveCollabRequest = collabRequestRepository.save(saveCollabRequest);

        Artist receiverArtist = artistRepository.findByArtistId(collabRequestInput.getReceiverId());
        if (receiverArtist == null) {
            throw new RecordNotFoundException("No artist found for the receiver id.");
        }

        notificationService.addNotification(Notification.builder()
                                                    .artistId(artistId)
                                                    .redirectUrl(String.format(Constants.REDIRECT_URL_MAP.get(
                                                            Constants.COLLAB_PAGE_URL), collabId))
                                                    .notifType(Constants.NOTIFICATION_TYPES.COLLAB_REQUEST.toString())
                                                    .notifRead(false)
                                                    .notificationData(String.format("%s send you a collab request",
                                                            receiverArtist.getFirstName()))
                                                    .build());

        return saveCollabRequest;
    }

    @Override
    public CollabRequest rejectRequest(String artistId, long rejectRequestId) {
        Optional<CollabRequest> collabRejectedByReceiver= collabRequestRepository.findById(rejectRequestId);
        if(collabRejectedByReceiver.isPresent()) {
            CollabRequest collabRequest = collabRejectedByReceiver.get();
            if( collabRequest.getReceiverId().equals(artistId) && collabRequest.getStatus().equals(Enums.CollabStatus.PENDING.toString())) {
                CollabRequest rejectCollabRequest = collabRequest.toBuilder()
                                                            .senderId(collabRequest.getSenderId()).receiverId(artistId)
                                                            .status(Enums.CollabStatus.REJECTED.toString())
                                                            .build();
                collabRequest = collabRequestRepository.save(rejectCollabRequest);
                return collabRequest;
            } else {
                if(!collabRequest.getReceiverId().equals(artistId)) {
                    throw new CollabRequestException("user not authorized to reject request");
                }
                else if(!collabRequest.getStatus().equals(Enums.CollabStatus.PENDING.toString())) {
                    throw new CollabRequestException("collab request status is not in pending state");
                }
                throw new CollabRequestException("error while rejecting request");
            }
        } else {
            throw new CollabRequestException("collab request id is not present");
        }
    }

    @Override
    public CollabRequest acceptRequest(String artistId, long acceptRequestId) {
        Optional<CollabRequest> collabAcceptedByReceiver = collabRequestRepository.findById(acceptRequestId);
        if(collabAcceptedByReceiver.isPresent()) {
            CollabRequest collabRequest = collabAcceptedByReceiver.get();
            if( collabRequest.getReceiverId().equals(artistId) && collabRequest.getStatus().equals(Enums.CollabStatus.PENDING.toString())) {
                CollabRequest rejectCollabRequest = collabRequest.toBuilder()
                                                            .senderId(collabRequest.getSenderId()).receiverId(artistId)
                                                            .status(Enums.CollabStatus.ACTIVE.toString())
                                                            .build();
                collabRequest = collabRequestRepository.save(rejectCollabRequest);
                return collabRequest;
            } else {
                if(!collabRequest.getReceiverId().equals(artistId)) {
                    throw new CollabRequestException("user not authorized to accept request");
                }
                else if(!collabRequest.getStatus().equals(Enums.CollabStatus.PENDING.toString())) {
                    throw new CollabRequestException("collab request status is not in pending state");
                }
                throw new CollabRequestException("error while accepting request");
            }
        } else {
            throw new CollabRequestException("collab request id is not present");
        }
    }

    @Override
    public List<CollabRequest> collabRequestsSearch(String artistId, CollabRequestSearch collabRequestSearch) {
        SpecificationBuilder<CollabRequest> builder =
                new SpecificationBuilder<CollabRequest>();
        if (collabRequestSearch.getCollabRequestId() != null) {
            builder.with("id", ":", collabRequestSearch.getCollabRequestId());
        }

        //get all collab request by sender id and authorizing it with the current user.
        if(!Strings.isNullOrEmpty(collabRequestSearch.getSenderId())  &&
                   collabRequestSearch.getSenderId().equalsIgnoreCase(artistId)) {
            builder.with("senderId", ":", collabRequestSearch.getSenderId());
        }

        //get all collab request by receiver id and authorizing it with the current user.
        if(!Strings.isNullOrEmpty(collabRequestSearch.getReceiverId()) &&
                   collabRequestSearch.getReceiverId().equalsIgnoreCase(artistId)) {
            builder.with("receiverId", ":", collabRequestSearch.getReceiverId());
        }

        if(!Strings.isNullOrEmpty(collabRequestSearch.getStatus())) {
            builder.with("status", ":", collabRequestSearch.getStatus());
        }

        Specification<CollabRequest> specification = builder.build();
        return collabRequestRepository.findAll(specification);

    }


}
