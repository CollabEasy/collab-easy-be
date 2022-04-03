package com.collab.project.model.artist;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.TypeDef;

import java.awt.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.*;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class SearchedArtistOutput implements Serializable {

    private String artistId;

    private String slug;

    private String firstName;

    private String lastName;

    private String profilePicUrl;

    private String bio;

    private Timestamp lastActive;

    private String gender;

    private List<String> skills;

    private String upForCollab;

    public SearchedArtistOutput(Artist artist, List<String> skills, String upForCollab) {
        this.artistId = artist.getArtistId();
        this.slug = artist.getSlug();
        this.firstName = artist.getFirstName();
        this.lastName = artist.getLastName();
        this.profilePicUrl = artist.getProfilePicUrl();
        this.bio = artist.getBio();
        this.gender = artist.getGender();
        this.lastActive = artist.getLastActive();
        this.skills = skills;
        this.upForCollab = upForCollab;
    }
}
