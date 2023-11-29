package com.collab.project.model.artist;

import lombok.*;

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

    public BasicArtist(Artist artist) {
        this.artistId = artist.getArtistId();
        this.slug = artist.getSlug();
        this.profilePicUrl = artist.getProfilePicUrl();
        this.firstName = artist.getFirstName();
        this.lastName = artist.getLastName();
    }
}
