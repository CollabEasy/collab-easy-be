package com.collab.project.model.response;

import lombok.Getter;

@Getter
public class SuccessResponse {
    Object data;
    String status;

    public SuccessResponse(String status) {
        this.status = "success";
    }

    public SuccessResponse(Object data) {
        this.data = data;
        this.status = "success";
    }

    public SuccessResponse() {
        this.status = "success";
    }
}
