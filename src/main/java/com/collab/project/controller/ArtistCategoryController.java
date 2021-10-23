package com.collab.project.controller;

import com.collab.project.model.artist.ArtistCategory;
import com.collab.project.model.inputs.ArtistCategoryInput;
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
@CrossOrigin
@RestController
// TODO : Fetch artist ID from JWT token instead of path variable.
@RequestMapping(value = "/api/v1/artist")
public class ArtistCategoryController {

    @Autowired
    private ArtistCategoryService artistCategoryService;

    @PostMapping
    @RequestMapping(value = "/arts", method = RequestMethod.POST)
    public ResponseEntity<SuccessResponse> addArtistCategory(@RequestBody ArtistCategoryInput artistCategoryInput) {
        Map<String,Boolean> savedResults = artistCategoryService.updateCategory(AuthUtils.getArtistId(), artistCategoryInput);
        return new ResponseEntity<>(new SuccessResponse(savedResults), HttpStatus.OK);
    }

    @GetMapping
    @RequestMapping(value = "/arts", method = RequestMethod.GET)
    public ResponseEntity<SuccessResponse> getArtistCategories() {
        List<String> arts = artistCategoryService.getArtistCategories(AuthUtils.getArtistId());
        return new ResponseEntity<>(new SuccessResponse(arts), HttpStatus.OK);
    }

    @GetMapping
    @RequestMapping(value = "/categories", method = RequestMethod.GET)
    public ResponseEntity<SuccessResponse> getBasicCategories() {
        List<String> arts = artistCategoryService.getDefaultCategories();
        return new ResponseEntity<>(new SuccessResponse(arts), HttpStatus.OK);
    }

    @RequestMapping(value = "/fetchByCategory", method = RequestMethod.POST)
    public ResponseEntity<SuccessResponse> getBasicCategories(
        @RequestBody ArtistCategoryInput artistCategoryInput) {
        Map<String,Object> arts = artistCategoryService.fetchArtistByArtName(artistCategoryInput);
        return new ResponseEntity<>(new SuccessResponse(arts), HttpStatus.OK);
    }

}
