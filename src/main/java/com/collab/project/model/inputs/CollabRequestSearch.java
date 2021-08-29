package com.collab.project.model.inputs;

import lombok.Data;

@Data
public class CollabRequestSearch {

    private final Long collabRequestId;
    private final String status;
    private final String senderId;
    private final String receiverId;

}
