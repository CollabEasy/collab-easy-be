package com.collab.project.model.collab;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.sql.Timestamp;

@Getter
@Setter
public class RequestData {
    private String message;

    private String collabTheme;

    private Timestamp tentativeTime;
}
