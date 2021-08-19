package com.collab.project.controller;

import com.collab.project.model.artist.ArtistCategory;
import com.collab.project.model.response.SuccessResponse;
import com.collab.project.service.ArtistCategoryService;
import com.collab.project.util.AuthUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
// TODO : Fetch artist ID from JWT token instead of path variable.
@RequestMapping(value = "/api/v1/artist")
public class ArtistCategoryController {

    @Autowired
    private ArtistCategoryService artistCategoryService;

    @PostMapping
    @RequestMapping(value = "/arts", method = RequestMethod.POST)
    public ResponseEntity<SuccessResponse> addArtistCategory(@RequestBody List<String> artNames) {
        List<ArtistCategory> savedResults = artistCategoryService.addCategory(AuthUtils.getArtistId(), artNames);
        return new ResponseEntity<>(new SuccessResponse(savedResults), HttpStatus.OK);
    }

    @GetMapping
    @RequestMapping(value = "/arts", method = RequestMethod.GET)
    public ResponseEntity<SuccessResponse> getArtistCategories() {
        List<String> arts = artistCategoryService.getArtistCategories(AuthUtils.getArtistId());
        return new ResponseEntity<>(new SuccessResponse(arts), HttpStatus.OK);
    }

}
