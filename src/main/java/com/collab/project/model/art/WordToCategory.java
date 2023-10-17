package com.collab.project.model.art;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "word_to_category")
@Getter
@Setter
public class WordToCategory {

    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "word")
    private String word;

    @NotNull
    @Column(name = "category_id")
    private Integer categoryId;

}
