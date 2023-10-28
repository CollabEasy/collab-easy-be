package com.collab.project.model.inputs;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.Data;
import org.hibernate.annotations.TypeDef;

@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class ProposalAnswerInput {

    String questionId;

    String answer;
}
