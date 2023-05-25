package com.collab.project.model.inputs;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.Data;
import org.hibernate.annotations.TypeDef;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import java.sql.Date;

@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class CategoryInput {
    Long id;

    String artName;

    String slug;

    String description;

    Boolean approved;
}
