package com.collab.project.model.contest;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.*;
import org.hibernate.annotations.TypeDef;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class ContestSubmissionResponse {

    private ContestSubmission contestSubmission;

    private int votes;

    private String firstName;

    private String lastName;
}
