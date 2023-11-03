package com.collab.project.model.proposal;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.TypeDef;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class ArtistProposals {

    List<ProposalResponse> created;

    List<ProposalResponse> interested;

    public ArtistProposals() {
        this.created = new ArrayList<>();
        this.interested = new ArrayList<>();
    }
}