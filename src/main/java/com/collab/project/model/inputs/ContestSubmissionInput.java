package com.collab.project.model.inputs;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ContestSubmissionInput {
    Long id;
    String contestSlug;
    String artistId;
    String artworkUrl;
    String description;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}