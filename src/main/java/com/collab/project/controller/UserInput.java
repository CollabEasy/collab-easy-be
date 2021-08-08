package com.collab.project.controller;

import lombok.Data;

@Data
public class UserInput {

    String userHandle;
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
}
