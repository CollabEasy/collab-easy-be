package com.collab.project.service.impl;

import com.collab.project.exception.CollabRequestException;
import com.collab.project.model.collab.CollabRequest;
import com.collab.project.model.enums.Enums;
import com.collab.project.model.inputs.AcceptRequestInput;
import com.collab.project.model.inputs.CollabRequestInput;
import com.collab.project.model.inputs.CollabRequestSearch;
import com.collab.project.model.inputs.RejectRequestInput;
import com.collab.project.repositories.CollabRequestRepository;
import com.collab.project.search.GenericSpecification;
import com.collab.project.search.SpecificationBuilder;
import com.collab.project.service.CollabService;
import com.google.common.base.Strings;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CollabServiceImpl implements CollabService {

    @Autowired
    private CollabRequestRepository collabRequestRepository;

    @Override
    public CollabRequest sendRequest(String artistId, CollabRequestInput collabRequestInput) {
        //TODO: Add validation on receiver id and handle idempotency on requestId
        List<String> status =  Arrays.asList( Enums.CollabStatus.PENDING.toString(),
                Enums.CollabStatus.ACTIVE.toString(), Enums.CollabStatus.REJECTED.toString());

        List<CollabRequest> collabRequestBySender = collabRequestRepository
                                                            .findBySenderIdAndRecevierIdAndStatusIn(artistId, collabRequestInput.getRecevierId(), status);
        List<CollabRequest> collabRequestByReceiver = collabRequestRepository
                                                              .findBySenderIdAndRecevierIdAndStatusIn(collabRequestInput.getRecevierId(), artistId, status);
        if(!collabRequestBySender.isEmpty() || !collabRequestByReceiver.isEmpty()) {
            throw new CollabRequestException(String.format("Collabaration already happened or Rejected"));
        }
        CollabRequest saveCollabRequest = CollabRequest.builder()
                .senderId(artistId).recevierId(collabRequestInput.getRecevierId())
                .collabDate(Timestamp.valueOf(collabRequestInput.getCollabDate()))
                .status(Enums.CollabStatus.PENDING.toString())
                .requestData(collabRequestInput.getRequestData())
                .build();
        saveCollabRequest = collabRequestRepository.save(saveCollabRequest);
        return saveCollabRequest;
    }

    @Override
    public CollabRequest rejectRequest(String artistId, RejectRequestInput rejectRequestInput) {
        Optional<CollabRequest> collabRejectedByReceiver= collabRequestRepository.findById(rejectRequestInput.getCollabRequestId());
        if(collabRejectedByReceiver.isPresent()) {
            CollabRequest collabRequest = collabRejectedByReceiver.get();
            if( collabRequest.getRecevierId() == artistId && collabRequest.getStatus() == Enums.CollabStatus.PENDING.toString()) {
                CollabRequest rejectCollabRequest = CollabRequest.builder()
                                                            .senderId(collabRequest.getSenderId()).recevierId(artistId)
                                                            .status(Enums.CollabStatus.REJECTED.toString())
                                                            .build();
                collabRequest = collabRequestRepository.save(rejectCollabRequest);
                return collabRequest;
            } else {
                if(collabRequest.getRecevierId() != artistId) {
                    throw new CollabRequestException(String.format("user not authorized to reject request"));
                }
                else if(collabRequest.getStatus() != Enums.CollabStatus.PENDING.toString()) {
                    throw new CollabRequestException(String.format("collab request status is not in pending state"));
                }
                throw new CollabRequestException(String.format("error while rejecting request"));
            }
        } else {
            throw new CollabRequestException(String.format("collab request id is not present"));
        }
    }

    @Override
    public CollabRequest acceptRequest(String artistId, AcceptRequestInput acceptRequestInput) {
        Optional<CollabRequest> collabAcceptedByReceiver = collabRequestRepository.findById(acceptRequestInput.getCollabRequestId());
        if(collabAcceptedByReceiver.isPresent()) {
            CollabRequest collabRequest = collabAcceptedByReceiver.get();
            if( collabRequest.getRecevierId() == artistId && collabRequest.getStatus() == Enums.CollabStatus.PENDING.toString()) {
                CollabRequest rejectCollabRequest = CollabRequest.builder()
                                                            .senderId(collabRequest.getSenderId()).recevierId(artistId)
                                                            .status(Enums.CollabStatus.REJECTED.toString())
                                                            .build();
                collabRequest = collabRequestRepository.save(rejectCollabRequest);
                return collabRequest;
            } else {
                if(collabRequest.getRecevierId() != artistId) {
                    throw new CollabRequestException(String.format("user not authorized to accept request"));
                }
                else if(collabRequest.getStatus() != Enums.CollabStatus.PENDING.toString()) {
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

        if(Strings.isNullOrEmpty(collabRequestSearch.getSenderId())) {
            builder.with("sender_id", ":", new Integer(collabRequestSearch.getStatus()));
        }

        if(Strings.isNullOrEmpty(collabRequestSearch.getReceiverId())) {
            builder.with("receiver_id", ":", new Integer(collabRequestSearch.getStatus()));
        }

        if(Strings.isNullOrEmpty(collabRequestSearch.getStatus())) {
            builder.with("status", ":", collabRequestSearch.getStatus());
        }

        Specification<CollabRequest> specification = builder.build();
        return collabRequestRepository.findAll(specification);

    }


}
