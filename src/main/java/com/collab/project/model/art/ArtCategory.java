package com.collab.project.model.art;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "art_categories")
@Getter
@Setter
public class ArtCategory {
    @Id
    private String artName;

    private String description;

    private Boolean approved;
}
