package com.collab.project.model.inputs;

import com.collab.project.model.collab.RequestData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.json.JSONObject;

import javax.validation.Valid;
import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class CollabRequestInput  {

    private String receiverId;
    private LocalDateTime collabDate;
    @Valid
    private RequestData requestData;
}
