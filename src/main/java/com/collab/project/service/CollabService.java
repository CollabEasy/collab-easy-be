package com.collab.project.service;


import com.collab.project.model.collab.CollabRequest;
import com.collab.project.model.collab.CollabRequestOutput;
import com.collab.project.model.inputs.CollabRequestInput;
import com.collab.project.model.inputs.CollabRequestSearch;

import java.util.List;

public interface CollabService {

    public CollabRequest sendRequest(String artistId, CollabRequestInput collabRequestInput);

    public CollabRequest updateRequest(String artistId, CollabRequest collabRequestInput);

    public CollabRequest rejectRequest(String artistId, String rejectRequestId);

    public CollabRequest acceptRequest(String artistId, String acceptRequestId);

    public CollabRequestOutput collabRequestsSearch(String artistId, CollabRequestSearch collabRequestSearch);

}
