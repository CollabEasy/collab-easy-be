package com.collab.project.controller;

import com.collab.project.model.artist.ArtistPreference;
import com.collab.project.service.ArtistPreferencesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(value = "/api/v1/artist")
public class ArtistPreferencesController {

    @Autowired
    ArtistPreferencesService artistPreferencesService;

    @PostMapping
    @RequestMapping("/{artistId}/preferences")
    public ResponseEntity<ArtistPreference> updateArtistPreferences(@PathVariable String artistId,
                                                                   @RequestBody ArtistPreference artistPreference) {
        artistPreferencesService.updateArtistPreferences(artistId, artistPreference);
        return new ResponseEntity<>(artistPreference, HttpStatus.OK);
    }

    @GetMapping
    @RequestMapping("/{artistId}/preferences")
    public ResponseEntity<ArtistPreference> getArtistPreferences(@PathVariable String artistId) {
        ArtistPreference artistPreference = artistPreferencesService.getArtistPreferences(artistId);
        return new ResponseEntity<>(artistPreference, HttpStatus.OK);
    }

}
