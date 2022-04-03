package com.collab.project.model.inputs;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.Data;
import org.hibernate.annotations.TypeDef;


@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class ArtistSocialProspectusInput {
    String handle;
    String artistId;
    Long socialPlatformId;
    String description;
    String upForCollab;
}
