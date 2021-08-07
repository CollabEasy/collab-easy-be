package com.collab.project.model.collab;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
public class RequestData {
    @NonNull
    @Column(nullable = false)
    String message;

    String collabTheme;
    @NonNull
    @Column(nullable = false)
    Long tentativeTime;
}
