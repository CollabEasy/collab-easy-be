package com.collab.project.model.socialprospectus;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;


@Entity
@Table(name = "social_platforms")
@Getter
@Setter
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

    @Column(name = "description")
    private String description;

    @Column(name = "approved")
    private Boolean approved;

    public SocialPlatform(Long id, String name, String description, Boolean approved) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.approved = approved;
    }
}
