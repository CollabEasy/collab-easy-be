package com.collab.project.model.art;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "art_categories")
@Getter
@Setter
public class ArtCategory {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    Long id;

    @Column(name = "name")
    private String artName;

    @Column(name = "description")
    private String description;

    @Column(name = "approved")
    private Boolean approved;
}
