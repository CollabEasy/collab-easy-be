package com.collab.project.model.collab;

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
public class CollabRequestsStatus {

    List<CollabRequestResponse> all;

    List<CollabRequestResponse> active;

    List<CollabRequestResponse> pending;

    List<CollabRequestResponse> rejected;

    List<CollabRequestResponse> completed;

    public CollabRequestsStatus() {
        this.all = new ArrayList<>();
        this.active = new ArrayList<>();
        this.pending = new ArrayList<>();
        this.rejected = new ArrayList<>();
        this.completed = new ArrayList<>();
    }
}