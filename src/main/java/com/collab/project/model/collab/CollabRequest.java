package com.collab.project.model.collab;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.sql.Timestamp;

@Entity
@Table(name = "collab-requests")
@Getter
@Setter
public class CollabRequest {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    Long id;
    @NonNull
    @Column(nullable = false, unique = true)
    private String requestId;
    @NonNull
    @Column(nullable = false, unique = true)
    private String senderId;
    @NonNull
    @Column(nullable = false, unique = true)
    private String recevierId;

    private String requestData;

    private Timestamp scheduledAt;

    private Boolean accepted;

    private Timestamp createdAt;

    private Timestamp updatedAt;
}
