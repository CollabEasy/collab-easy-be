package com.collab.project.model.inputs;

import lombok.Data;

@Data
public class CollabRequestSearch {

    //private final String requestId;
    //
    //private final String recevierId;
    Long collabRequestId; //auto increment
    String status;
    String senderId;
    String receiverId;

}
