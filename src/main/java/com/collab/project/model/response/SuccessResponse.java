package com.collab.project.model.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SuccessResponse {
    Object data;
    String status;

    public SuccessResponse(Object data) {
        this.data = data;
        this.status = "success";
    }

    public SuccessResponse() {
        this.status = "success";
    }
}
