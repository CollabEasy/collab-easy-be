package com.collab.project.model.proposal;

import com.collab.project.model.contest.ContestSubmission;
import com.collab.project.model.enums.Enums;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.*;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class ProposalResponse {

    private Proposal proposal;

    private String creatorFirstName;

    private String creatorLastName;

    private String creatorSlug;

    private String creatorProfilePicUrl;
}
