package com.collab.project.model.art;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "art_categories")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ArtCategory {
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "art_name")
    private String artName;

    @Column(name = "description")
    private String description;

    @Column(name = "approved")
    private Boolean approved;
}
