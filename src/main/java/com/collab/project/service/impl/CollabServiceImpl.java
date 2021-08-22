package com.collab.project.service.impl;

import com.collab.project.exception.CollabRequestException;
import com.collab.project.model.collab.CollabRequest;
import com.collab.project.model.enums.Enums;
import com.collab.project.model.inputs.AcceptRequestInput;
import com.collab.project.model.inputs.CollabRequestInput;
import com.collab.project.model.inputs.RejectRequestInput;
import com.collab.project.repositories.CollabRequestRepository;
import com.collab.project.service.CollabService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

@Service
public class CollabServiceImpl implements CollabService {

    @Autowired
    private CollabRequestRepository collabRequestRepository;


    @Override
    public CollabRequest sendRequest(String artistId, CollabRequestInput collabRequestInput) {
        List<String> status =  Arrays.asList( Enums.CollabStatus.PENDING.toString(),
                Enums.CollabStatus.ACTIVE.toString(), Enums.CollabStatus.REJECTED.toString());

        List<CollabRequest> collabRequestBySender = collabRequestRepository
                                                            .findBySenderIdAndRecevierIdAndStatusIn(artistId, collabRequestInput.getRecevierId(), status);
        List<CollabRequest> collabRequestByReceiver = collabRequestRepository
                                                              .findBySenderIdAndRecevierIdAndStatusIn(collabRequestInput.getRecevierId(), artistId, status);
        if(!collabRequestBySender.isEmpty() || !collabRequestByReceiver.isEmpty()) {
            throw new CollabRequestException(String.format("Collabaration already happened"));
        }
        CollabRequest saveCollabRequest = CollabRequest.builder().requestId(collabRequestInput.getRequestId())
                .senderId(artistId).recevierId(collabRequestInput.getRecevierId())
                .collabDate(Timestamp.valueOf(collabRequestInput.getCollabDate()))
                .status(Enums.CollabStatus.PENDING.toString())
                .requestData(collabRequestInput.getRequestData())
                .build();
        collabRequestRepository.save(saveCollabRequest);
        return saveCollabRequest;
    }

    @Override
    public CollabRequest rejectRequest(String artistId, RejectRequestInput rejectRequestInput) {
        return null;
    }

    @Override
    public CollabRequest acceptRequest(String artistId, AcceptRequestInput acceptRequestInput) {
       return null;
    }
}
