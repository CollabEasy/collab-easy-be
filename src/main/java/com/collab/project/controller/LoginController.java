package com.collab.project.controller;


import com.collab.project.service.ArtistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

    @Autowired
    ArtistService artistService;

    @RequestMapping("/login")
    public void login(ArtistInput input) {
        artistService.createArtist(input);
        return;
    }
}
