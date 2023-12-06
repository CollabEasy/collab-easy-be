package com.collab.project.model.proposal;

import com.collab.project.model.inputs.CollabRequestInput;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AcceptProposalInput {
    private String artistId;

    private CollabRequestInput collabRequestInput;
}
