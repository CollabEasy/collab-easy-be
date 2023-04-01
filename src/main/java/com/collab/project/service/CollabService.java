package com.collab.project.service;


import com.collab.project.model.collab.CollabRequest;
import com.collab.project.model.collab.CollabRequestOutput;
import com.collab.project.model.inputs.CollabRequestInput;
import com.collab.project.model.inputs.CollabRequestSearch;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;

public interface CollabService {

    public CollabRequest sendRequest(String artistId, CollabRequestInput collabRequestInput) throws JsonProcessingException;

    public CollabRequest updateRequest(String artistId, CollabRequest collabRequestInput);

    public CollabRequest rejectRequest(String artistId, String rejectRequestId);

    public CollabRequest acceptRequest(String artistId, String acceptRequestId);

    public CollabRequest completeRequest(String artistId, String completeRequestId);

    public void cancelRequest(String artistId, String requestId);

    public CollabRequestOutput collabRequestsSearch(String artistId, CollabRequestSearch collabRequestSearch);

}
