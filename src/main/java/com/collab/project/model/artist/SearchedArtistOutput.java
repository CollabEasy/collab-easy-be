package com.collab.project.model.artist;

import com.collab.project.security.Role;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import org.hibernate.annotations.TypeDef;
import org.springframework.util.StringUtils;

import javax.persistence.Column;
import javax.persistence.Transient;
import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;

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

    public SearchedArtistOutput(Artist artist) {
        this.artistId = artist.getArtistId();
        this.slug = artist.getSlug();
        this.firstName = artist.getFirstName();
        this.lastName = artist.getLastName();
        this.profilePicUrl = artist.getProfilePicUrl();
        this.bio = artist.getBio();
        this.gender = artist.getGender();
        this.lastActive = artist.getLastActive();
    }
}
