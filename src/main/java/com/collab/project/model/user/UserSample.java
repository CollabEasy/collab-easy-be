package com.collab.project.model.user;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "user-samples")
@Getter
@Setter
public class UserSample {
    @NonNull
    @Column(nullable = false, unique = true)
    private String userId;
    @NonNull
    @Column(nullable = false, unique = true)
    private String url;

    private Long updatedAt;
}