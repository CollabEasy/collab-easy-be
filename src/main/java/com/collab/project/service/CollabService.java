package com.collab.project.service;


import com.collab.project.model.collab.CollabEligibilityOutput;
import com.collab.project.model.collab.CollabRequest;
import com.collab.project.model.collab.CollabRequestOutput;
import com.collab.project.model.collab.CollabRequestResponse;
import com.collab.project.model.inputs.CollabRequestInput;
import com.collab.project.model.inputs.CollabRequestSearch;
import com.fasterxml.jackson.core.JsonProcessingException;

import javax.mail.MessagingException;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.Map;

public interface CollabService {

    public CollabRequestResponse sendRequest(String artistId, CollabRequestInput collabRequestInput) throws JsonProcessingException;

    public CollabRequestResponse sendCollabRequest(String artistId, CollabRequestInput collabRequestInput) throws JsonProcessingException;

    public CollabRequestResponse updateRequest(String artistId, CollabRequest collabRequestInput);

    public CollabRequestResponse rejectRequest(String artistId, String rejectRequestId);

    public CollabRequestResponse acceptRequest(String artistId, String acceptRequestId);

    public CollabRequestResponse completeRequest(String artistId, String completeRequestId);

    public void cancelRequest(String artistId, String requestId);

    public CollabRequestOutput collabRequestsSearch(String artistId, CollabRequestSearch collabRequestSearch);

    public CollabEligibilityOutput canCreateNewCollabRequest(String user1, String user2slug);

    Map<String, List<CollabRequestResponse>> fetchCollabsByDate(String artistId, boolean fetchAll);
}
