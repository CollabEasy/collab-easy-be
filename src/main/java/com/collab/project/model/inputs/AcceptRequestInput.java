package com.collab.project.model.inputs;

import lombok.Data;
import org.json.JSONObject;

@Data
public class AcceptRequestInput {

    //private final String requestId;
    //
    //private final String receiverId;
    private final Long collabRequestId; //auto increment
}
