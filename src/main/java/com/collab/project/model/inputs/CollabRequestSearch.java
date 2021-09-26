package com.collab.project.model.inputs;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CollabRequestSearch {
    @JsonProperty("collabRequestId")
    private Long collabRequestId;
    @JsonProperty("status")
    private String status;
    @JsonProperty("senderId")
    private String senderId;
    @JsonProperty("receiverId")
    private String receiverId;
}
