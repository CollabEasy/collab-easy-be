package com.collab.project.controller;


import com.collab.project.model.artist.Artist;
import com.collab.project.service.ArtistService;
import com.collab.project.util.AuthUtils;
import com.collab.project.util.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequestMapping(method = RequestMethod.POST, value = "/api")
@RestController()
public class LoginController {

    @Autowired
    ArtistService artistService;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    AuthUtils authUtils;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<?> login(@RequestBody ArtistInput input) {
        Artist artist = artistService.createArtist(input);
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(artist.getArtistId(), ""));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtUtils.generateJwtToken(authentication);
        return ResponseEntity.ok(token);
    }

    @RequestMapping(value = "/get/Details", method = RequestMethod.GET)
    public ResponseEntity<?> update() {
        String artistId = authUtils.getArtistId();
        return ResponseEntity.ok(artistId);
    }
}
