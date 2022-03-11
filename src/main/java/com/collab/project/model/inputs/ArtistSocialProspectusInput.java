package com.collab.project.model.inputs;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ArtistSocialProspectusInput {
    private Long socialPlatformId;
    private String artistId;
    private String handle;
    private String description;
}
