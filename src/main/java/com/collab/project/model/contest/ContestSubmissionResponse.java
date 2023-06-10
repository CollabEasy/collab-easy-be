package com.collab.project.model.contest;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.*;
import org.hibernate.annotations.TypeDef;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class ContestSubmissionResponse {

    private ContestSubmission submission;

    private String firstName;

    private String lastName;
}
