package com.collab.project.model.inputs;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ContestInput {
    Long id;
    String contestSlug;
    String title;
    String description;
    LocalDateTime startDate;
    LocalDateTime endDate;
}