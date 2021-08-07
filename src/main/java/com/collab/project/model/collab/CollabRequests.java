package com.collab.project.model.collab;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "collab-requests")
@Getter
@Setter
public class CollabRequests {
    @NonNull
    @Column(nullable = false, unique = true)
    String requestId;
    @NonNull
    @Column(nullable = false, unique = true)
    String senderId;
    @NonNull
    @Column(nullable = false, unique = true)
    String recevierId;

    RequestData requestData;

    Long scheduledAt;

    Boolean accepted;

    Long createdAt;

    Long updatedAt;
}
