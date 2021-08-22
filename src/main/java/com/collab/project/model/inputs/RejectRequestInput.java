package com.collab.project.model.inputs;

import lombok.Data;
import org.json.JSONObject;

@Data
public class RejectRequestInput {

    private final String requestId;
    private final String senderId;
    private final String recevierId;
    private final JSONObject description;
}
