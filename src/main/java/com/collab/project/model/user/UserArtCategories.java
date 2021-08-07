package com.collab.project.model.user;

import lombok.Getter;
import lombok.NonNull;

import javax.persistence.Column;

@Getter
public class UserArtCategories {
    @NonNull
    @Column(nullable = false, unique = true)
    String userId;
    @NonNull
    @Column(nullable = false, unique = true)
    String artId;
}
