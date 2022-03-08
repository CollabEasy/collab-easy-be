package com.collab.project.model.collab;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.TypeDef;

import java.io.Serializable;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class CollabRequestOutput implements Serializable {

    CollabRequestsStatus sent;

    CollabRequestsStatus received;
}
