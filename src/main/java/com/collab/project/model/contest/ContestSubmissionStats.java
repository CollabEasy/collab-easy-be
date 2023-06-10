package com.collab.project.model.contest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ContestSubmissionStats {

    ContestSubmission submission;

    String firstName;

    String lastName;

    int votes;
}
