package com.collab.project.model.art;

import io.jsonwebtoken.lang.Strings;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Locale;

@Entity
@Table(name = "art_categories")
@Getter
@Setter
public class ArtCategory {
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "art_name")
    private String artName;

    @Column(name = "slug")
    private String slug;

    @Column(name = "description")
    private String description;

    @Column(name = "approved")
    private Boolean approved;

    public ArtCategory() {
        updateSlug();
    }

    public ArtCategory(Long id, String artName, String slug, String description, Boolean approved) {
        this.id = id;
        this.artName = artName;
        updateSlug();
        this.description = description;
        this.approved = approved;
    }

    private void updateSlug() {
        if (artName == null) return;
        this.slug = Strings.replace(artName.toLowerCase(Locale.ROOT), " ", "-");
    }
}
