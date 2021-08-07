package com.collab.project.model.art;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "art-categories")
@Getter
@Setter
public class ArtCategories {
    String name;

    String description;

    Boolean approved;
}
