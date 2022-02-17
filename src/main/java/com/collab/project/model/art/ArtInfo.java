package com.collab.project.model.art;


import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.*;
import org.hibernate.annotations.TypeDef;

import java.sql.Timestamp;

@Data
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class ArtInfo {

    String caption;

    String fileType;

    String originalUrl;

    String thumbnailUrl;

    Timestamp createdAt;
}