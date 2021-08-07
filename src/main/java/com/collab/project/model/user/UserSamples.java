package com.collab.project.model.user;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import javax.persistence.Column;

@Getter
@Setter
public class UserSamples {
    @NonNull
    @Column(nullable = false, unique = true)
    String userId;
    @NonNull
    @Column(nullable = false, unique = true)
    String url;

    Long updatedAt;
}
