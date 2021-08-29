package com.collab.project.model.inputs;

import lombok.Data;
import org.springframework.web.bind.annotation.RequestBody;

@Data
public class CollabRequestSearch {

    //private final String requestId;
    //
    //private final String receiverId;
    private final Long collabRequestId; //auto increment
    private final String status;
    private final String senderId;
    private final String receiverId;

}
