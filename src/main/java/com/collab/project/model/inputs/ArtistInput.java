package com.collab.project.model.inputs;

import lombok.Data;

@Data
public class ArtistInput {
    String idToken;
    String artistHandle;
    String artistId;
    String firstName;
    String lastName;
    String email;
    Long phoneNumber;
    String country;
    String profilePicUrl;
    String timezone;
    String bio;
    Integer age;
    String gender;
    Boolean valid;
}