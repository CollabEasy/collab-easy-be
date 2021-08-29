package com.collab.project.model.inputs;

import com.collab.project.model.collab.RequestData;
import lombok.Data;
import lombok.NonNull;
import org.json.JSONObject;

import javax.validation.Valid;
import java.time.LocalDateTime;


@Data
public class CollabRequestInput  {

    //private final String requestId;
    //private final String senderId;
    private final String receiverId;
    private final LocalDateTime collabDate;
    @Valid
    private final RequestData requestData;
}
