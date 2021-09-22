package com.collab.project.service.impl;

import com.collab.project.exception.CollabRequestException;
import com.collab.project.model.collab.CollabRequest;
import com.collab.project.model.enums.Enums;
import com.collab.project.model.inputs.CollabRequestInput;
import com.collab.project.model.inputs.CollabRequestSearch;
import com.collab.project.model.notification.Notification;
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

@Service
public class CollabServiceImpl implements CollabService {

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
            throw new CollabRequestException(String.format("Collabaration already happened or Rejected"));
        }
        CollabRequest saveCollabRequest = CollabRequest.builder()
                .senderId(artistId).receiverId(collabRequestInput.getReceiverId())
                .collabDate(Timestamp.valueOf(collabRequestInput.getCollabDate()))
                .status(Enums.CollabStatus.PENDING.toString())
                .requestData(collabRequestInput.getRequestData())
                .build();
        saveCollabRequest = collabRequestRepository.save(saveCollabRequest);

        notificationService.addNotification(Notification.builder()
                                                    .artistId(artistId)
                                                    .redirectId(collabRequestInput.getReceiverId())
                                                    .notifType("collabRequest")
                                                    .notifRead(false)
                                                    .notificationData(String.format("%s send you a collab request", artistId))
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

    @Override
    public List<CollabRequest> collabRequestsSearch(String artistId, CollabRequestSearch collabRequestSearch) {
        SpecificationBuilder<CollabRequest> builder =
                new SpecificationBuilder();
        if (collabRequestSearch.getCollabRequestId() != null) {
            builder.with("id", ":", new Long(collabRequestSearch.getCollabRequestId()));
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
