package com.collab.project.model.artist;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BasicArtist {

    String artistId;
    String slug;
    String profilePicUrl;
    String firstName;
    String lastName;
    List<String> skills;

    public BasicArtist(Artist artist, List<String> skills) {
        this.artistId = artist.getArtistId();
        this.slug = artist.getSlug();
        this.profilePicUrl = artist.getProfilePicUrl();
        this.firstName = artist.getFirstName();
        this.lastName = artist.getLastName();
        this.skills = skills;
    }
}
