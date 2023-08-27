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
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@CrossOrigin
@RequestMapping(method = RequestMethod.POST, value = "/api/v1/artist")
@RestController
public class ArtistController {

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
    public ResponseEntity<?> validate(@RequestBody ArtistInput input) throws NoSuchAlgorithmException {
        Boolean isValid = googleUtils.isValid(input);
        if (isValid) {
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


    @RequestMapping(value = "/avatar/update", method = RequestMethod.POST, consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<SuccessResponse> uploadProfilePicture(@RequestPart MultipartFile filename) throws IOException, NoSuchAlgorithmException {
        Artist artist = artistService.updateProfilePicture(AuthUtils.getArtistId(), filename);
        Map<String, Object> hashMap = mapper.convertValue(artist, Map.class);
        return new ResponseEntity<>(new SuccessResponse(hashMap), HttpStatus.OK);
    }
}
