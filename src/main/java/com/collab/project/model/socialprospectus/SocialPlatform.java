package com.collab.project.model.socialprospectus;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.*;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;


@Entity
@Table(name = "social_platforms")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class SocialPlatform {
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name")
    private String name;

    @NotNull
    @Column(name = "base_url")
    private String base_url;

    @Column(name = "description")
    private String description;

    @Column(name = "approved")
    private Boolean approved;

    @Column(name="created_at", updatable = false, insertable = false, nullable = false)
    private Timestamp createdAt;

    @Column(name="updated_at", updatable = false, insertable = false, nullable = false)
    private Timestamp updatedAt;

    public SocialPlatform(Long id, String name, String base_url, String description, Boolean approved) {
        this.id = id;
        this.name = name;
        this.base_url = base_url;
        this.description = description;
        this.approved = approved;
    }
}
