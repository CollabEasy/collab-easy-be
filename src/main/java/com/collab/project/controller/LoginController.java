package com.collab.project.controller;


import com.collab.project.model.artist.Artist;
import com.collab.project.model.inputs.ArtistInput;
import com.collab.project.model.response.SuccessResponse;
import com.collab.project.service.ArtistService;
import com.collab.project.util.AuthUtils;
import com.collab.project.util.GoogleUtils;
import com.collab.project.util.JwtUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@CrossOrigin
@RequestMapping(method = RequestMethod.POST, value = "/api")
@RestController
public class LoginController {

    @Autowired
    ArtistService artistService;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    AuthUtils authUtils;

    @Autowired
    GoogleUtils googleUtils;

    @Autowired
    ObjectMapper mapper;

    @RequestMapping(value = "/test1", method = RequestMethod.POST)
    public ResponseEntity<?> login(@RequestBody ArtistInput input) {
        Artist artist = artistService.createArtist(input);
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(artist.getArtistId(), ""));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtUtils.generateJwtToken(authentication);
        JSONObject object = new JSONObject("token", token);
        return new ResponseEntity<>(new SuccessResponse(token), HttpStatus.OK);
    }

    @RequestMapping(value = "/get/Details", method = RequestMethod.GET)
    public ResponseEntity<?> update() {
        String artistId = authUtils.getArtistId();
        return ResponseEntity.ok(artistId);
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<?> validate(@RequestBody ArtistInput input) {
        Boolean isValid = googleUtils.isValid(input);
        if (isValid) {
            Artist artist = artistService.createArtist(input);
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(artist.getArtistId(), ""));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String token = jwtUtils.generateJwtToken(authentication);
            Map<String, Object> hashMap = mapper.convertValue(artist, Map.class);
            hashMap.put("detailsUpdated", artist.areDetailsUpdated());
            hashMap.put("isNewUser", artist.getNewUser());
            hashMap.put("token", token);

            return new ResponseEntity<>(new SuccessResponse(hashMap, "SUCCESS"), HttpStatus.OK);
        }
        return new ResponseEntity<>(new SuccessResponse("Invalid Login", "FAILURE"),
            HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public ResponseEntity<?> update(@RequestBody ArtistInput input) {
        return new ResponseEntity<>(
            new SuccessResponse(artistService.updateArtist(input) ? "Details Updated SuccessFully"
                : "Failure while Details Update",
                "SUCCESS"), HttpStatus.OK);
    }
}
