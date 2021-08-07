package com.collab.project.model.user;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "user-art-categories")
@Getter
public class UserArtCategories {
    @NonNull
    @Column(nullable = false, unique = true)
    String userId;
    @NonNull
    @Column(nullable = false, unique = true)
    String artId;
}
