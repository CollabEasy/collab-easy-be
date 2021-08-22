package com.collab.project.service;


import com.collab.project.model.collab.CollabRequest;
import com.collab.project.model.inputs.AcceptRequestInput;
import com.collab.project.model.inputs.CollabRequestInput;
import com.collab.project.model.inputs.RejectRequestInput;

public interface CollabService {

    public CollabRequest sendRequest(String artistId, CollabRequestInput collabRequestInput);

    public CollabRequest rejectRequest(String artistId, RejectRequestInput rejectRequestInput);

    public CollabRequest acceptRequest(String artistId, AcceptRequestInput acceptRequestInput);

}
