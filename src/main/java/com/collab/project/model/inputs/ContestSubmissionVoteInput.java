package com.collab.project.model.inputs;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ContestSubmissionVoteInput {
    Long id;
    String contestSlug;
    Long submissionId;
    String artistId;
    Boolean vote;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}