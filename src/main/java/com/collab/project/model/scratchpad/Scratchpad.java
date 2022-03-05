package com.collab.project.model.scratchpad;


import com.vladmihalcea.hibernate.type.json.JsonBinaryType;

import lombok.*;
import org.hibernate.annotations.TypeDef;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Table(name = "scratchpads")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class Scratchpad implements Serializable {
    @Id
    private String id;

    @Column(nullable = true, unique = true)
    private String artistId;

    private String content;

    @Column(name="created_at", updatable = false, insertable = false, nullable = false)
    private Timestamp createdAt;

    @Column(name="updated_at", updatable = false, insertable = false, nullable = false)
    private Timestamp updatedAt;

    @Column(name="deleted_at", updatable = false, insertable = false, nullable = false)
    private Timestamp deletedAt;

    public Scratchpad(String artistId, String content) {
        this.artistId = artistId;
        this.content = content;
    }
}
