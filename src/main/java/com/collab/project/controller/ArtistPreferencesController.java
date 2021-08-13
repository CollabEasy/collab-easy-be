package com.collab.project.controller;

import com.collab.project.model.artist.ArtistPreference;
import com.collab.project.model.response.ArtistPrefResponse;
import com.collab.project.model.response.SuccessResponse;
import com.collab.project.service.ArtistPreferencesService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
// TODO : Fetch artist ID from JWT token instead of path variable.
@RequestMapping(value = "/api/v1/artist/{artistId}")
public class ArtistPreferencesController {

    @Autowired
    ArtistPreferencesService artistPreferencesService;

    @PostMapping
    @RequestMapping(value = "/preferences", method = RequestMethod.POST)
    public ResponseEntity<SuccessResponse> updateArtistPreferences(@PathVariable String artistId,
                                                                   @RequestBody Map<String, Object> artistPreferences) throws JsonProcessingException {
        artistPreferencesService.updateArtistPreferences(artistId, artistPreferences);
        return new ResponseEntity<>(new SuccessResponse(), HttpStatus.OK);
    }

    @PostMapping
    @RequestMapping(value = "/preference/{settingName}", method = RequestMethod.POST)
    public ResponseEntity<SuccessResponse> updateArtistPreferences(@PathVariable String artistId,
                                                                    @PathVariable String settingName,
                                                                    @RequestBody String settingValue)  {
        artistPreferencesService.updateArtistPreferences(new ArtistPreference(artistId, settingName, settingValue));
        return new ResponseEntity<>(new SuccessResponse(new ArtistPreference(artistId, settingName, settingValue)), HttpStatus.OK);
    }

    @GetMapping
    @RequestMapping(value = "/preference/{settingName}", method = RequestMethod.GET)
    public ResponseEntity<SuccessResponse> getArtistPreferences(@PathVariable String artistId,
                                                                   @PathVariable String settingName) {
        ArtistPreference artistPreference = artistPreferencesService.getArtistPreferences(artistId, settingName);
        ArtistPrefResponse artistPrefResponse = new ArtistPrefResponse(artistPreference);
        System.out.println(artistPrefResponse.toString());
        return new ResponseEntity<>(new SuccessResponse(artistPrefResponse), HttpStatus.OK);
    }

    @GetMapping
    @RequestMapping(value = "/preferences", method = RequestMethod.GET)
    public ResponseEntity<SuccessResponse> getArtistPreferences(@PathVariable String artistId) {
        List<ArtistPreference> artistPreferences = artistPreferencesService.getArtistPreferences(artistId);
        return new ResponseEntity<>(new SuccessResponse(new ArtistPrefResponse(artistPreferences)), HttpStatus.OK);
    }

}
