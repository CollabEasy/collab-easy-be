package com.collab.project.model.inputs;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ContestSubmissionInput {
    Long id;
    String contestSlug;
    String artistId;
    String artworkUrl;
    String description;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}