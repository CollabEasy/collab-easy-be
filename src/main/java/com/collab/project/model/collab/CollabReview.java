package com.collab.project.model.collab;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

@Entity
@Table(name = "collab-reviews")
@Getter
@Setter
public class CollabReview {
    @Id
    private String requestId;

    private String artistId;

    private int rating;

    private String review;

    private Timestamp createdAt;
}
