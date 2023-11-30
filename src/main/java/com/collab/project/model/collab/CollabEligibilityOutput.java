package com.collab.project.model.collab;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.TypeDef;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class CollabEligibilityOutput {

    List<CollabRequestResponse> activeCollabRequests;

    String otherUserId;

    boolean eligible;

    int allowedRequests;
}
