package com.collab.project.model.inputs;

import lombok.Data;

@Data
public class SocialPlatformInput {
    Long id;
    String name;
    String base_url;
    String description;
    Boolean approved;
}
