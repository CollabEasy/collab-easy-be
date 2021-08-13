package com.collab.project.controller;

import com.collab.project.helpers.SerdeHelper;
import com.collab.project.model.artist.ArtistPreference;
import com.collab.project.service.ArtistPreferencesService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
// TODO : Fetch artist ID from JWT token instead of path variable.
@RequestMapping(value = "/api/v1/artist/{artistId}")
public class ArtistPreferencesController {

    @Autowired
    ArtistPreferencesService artistPreferencesService;

    @PostMapping
    @RequestMapping(value = "/preferences", method = RequestMethod.POST)
    public ResponseEntity<ArtistPreference> updateArtistPreferences(@PathVariable String artistId,
                                                                   @RequestBody ArtistPreference artistPreference) {
        artistPreferencesService.updateArtistPreferences(artistPreference);
        return new ResponseEntity<>(artistPreference, HttpStatus.OK);
    }

    @PostMapping
    @RequestMapping(value = "/preference/{settingName}", method = RequestMethod.POST)
    public ResponseEntity<ArtistPreference> updateArtistPreferences(@PathVariable String artistId,
                                                                    @PathVariable String settingName,
                                                                    @RequestBody Object settingValue) throws JsonProcessingException {
        artistPreferencesService.updateArtistPreferences(artistId, settingName, settingValue);
        String settingValueString = SerdeHelper.getJsonStringFromObject(settingValue);
        return new ResponseEntity<>(new ArtistPreference(artistId, settingName, settingValueString), HttpStatus.OK);
    }

    @GetMapping
    @RequestMapping(value = "/preference/{settingName}", method = RequestMethod.GET)
    public ResponseEntity<ArtistPreference> getArtistPreferences(@PathVariable String artistId,
                                                                 @PathVariable String settingName) {
        ArtistPreference artistPreference = artistPreferencesService.getArtistPreferences(artistId, settingName);
        return new ResponseEntity<>(artistPreference, HttpStatus.OK);
    }

    @GetMapping
    @RequestMapping(value = "/preferences", method = RequestMethod.GET)
    public ResponseEntity<List<ArtistPreference>> getArtistPreferences(@PathVariable String artistId) {
        List<ArtistPreference> artistPreference = artistPreferencesService.getArtistPreferences(artistId);
        return new ResponseEntity<>(artistPreference, HttpStatus.OK);
    }

}
