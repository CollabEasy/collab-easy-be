package com.collab.project.controller;


import com.collab.project.exception.RecordNotFoundException;
import com.collab.project.model.artist.Artist;
import com.collab.project.model.artist.ArtistPreference;
import com.collab.project.model.artist.SearchedArtistOutput;
import com.collab.project.model.inputs.ArtistInput;
import com.collab.project.model.response.ErrorResponse;
import com.collab.project.model.response.SuccessResponse;
import com.collab.project.service.ArtistCategoryService;
import com.collab.project.service.ArtistPreferencesService;
import com.collab.project.service.ArtistService;
import com.collab.project.util.AuthUtils;
import com.collab.project.util.GoogleUtils;
import com.collab.project.util.JwtUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.*;

import io.jsonwebtoken.lang.Strings;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@Slf4j
@CrossOrigin
@RequestMapping(method = RequestMethod.POST, value = "/api/v1/artist")
@RestController
public class LoginController {

    @Autowired
    ArtistService artistService;

    @Autowired
    ArtistPreferencesService artistPreferencesService;

    @Autowired
    ArtistCategoryService artistCategoryService;

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

    @RequestMapping(value = "/details", method = RequestMethod.GET)
    public ResponseEntity<?> fetchDetails(@RequestParam(required = false) String handle) {
        Artist artist = null;
        if (handle == null || handle.equals("")) {
           artist = artistService.getArtistById(authUtils.getArtistId());
        } else {
            artist = artistService.getArtistBySlug(handle);
        }
        if(artist == null) {
            return new ResponseEntity<>(new ErrorResponse("User not found", "NOT_FOUND"), HttpStatus.NOT_FOUND);
        }

        Map<String, Object> hashMap = new HashMap<>();
        if (handle == null || handle.equals("")) {
            hashMap = mapper.convertValue(artist, Map.class);
        } else {
            String settingValue = "";
            try {
                ArtistPreference preference = artistPreferencesService.getArtistPreferences(artist.getArtistId(), "upForCollaboration");
                settingValue = preference.getSettingValues();
            } catch (RecordNotFoundException e) {
                log.info("No artist preference found for artist ", artist.getArtistId());
            }
            List<String> categories = artistCategoryService.getArtistCategories(artist.getArtistId());
            SearchedArtistOutput output = new SearchedArtistOutput(artist, categories, settingValue);
            hashMap = mapper.convertValue(output, Map.class);
        }
        return new ResponseEntity<>(new SuccessResponse(hashMap), HttpStatus.OK);
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<?> validate(@RequestBody ArtistInput input) {
        System.out.println("Rabbal " + input);
        Boolean isValid = googleUtils.isValid(input);
        if (isValid) {
            System.out.println("Rabbal is valid ");
            Artist artist = artistService.createArtist(input);
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(artist.getArtistId(), ""));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String token = jwtUtils.generateJwtToken(authentication);
            Map<String, Object> hashMap = mapper.convertValue(artist, Map.class);

            hashMap.put("details_updated", artist.areDetailsUpdated());
            hashMap.put("token", token);

            return new ResponseEntity<>(new SuccessResponse(hashMap, "SUCCESS"), HttpStatus.OK);
        }
        System.out.println("Rabbal is not valid ");
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


    @RequestMapping(value = "/login/delete", method = RequestMethod.POST)
    public ResponseEntity<?> delete(@RequestBody ArtistInput input) {
        artistService.delete(input);
        return new ResponseEntity<>(new SuccessResponse("Deleted", "SUCCESS"),
            HttpStatus.OK);
    }
}
