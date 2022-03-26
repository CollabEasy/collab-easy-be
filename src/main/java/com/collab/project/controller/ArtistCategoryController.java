package com.collab.project.controller;

import com.collab.project.model.artist.Artist;
import com.collab.project.model.artist.ArtistCategory;
import com.collab.project.model.artist.SearchedArtistOutput;
import com.collab.project.model.inputs.ArtistCategoryInput;
import com.collab.project.model.response.SuccessResponse;
import com.collab.project.service.ArtistCategoryService;
import com.collab.project.util.AuthUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping(value = "/api/v1/artist")
public class ArtistCategoryController {

    @Autowired
    private ArtistCategoryService artistCategoryService;

    @PostMapping
    @RequestMapping(value = "/arts", method = RequestMethod.POST)
    public ResponseEntity<SuccessResponse> addArtistCategory(@RequestBody ArtistCategoryInput artistCategoryInput) {
        List<ArtistCategory> savedResults = artistCategoryService.addCategory(AuthUtils.getArtistId(), artistCategoryInput);
        return new ResponseEntity<>(new SuccessResponse(savedResults), HttpStatus.OK);
    }

    @GetMapping
    @RequestMapping(value = "/arts", method = RequestMethod.GET)
    public ResponseEntity<SuccessResponse> getArtistCategories(@RequestParam(required = false) String handle) {
        List<String> arts = artistCategoryService.getArtistCategories(
                handle != null ? handle : AuthUtils.getArtistId());
        return new ResponseEntity<>(new SuccessResponse(arts), HttpStatus.OK);
    }

    @GetMapping
    @RequestMapping(value = "/categories", method = RequestMethod.GET)
    public ResponseEntity<SuccessResponse> getBasicCategories() {
        List<String> arts = artistCategoryService.getDefaultCategories();
        return new ResponseEntity<>(new SuccessResponse(arts), HttpStatus.OK);
    }

    @GetMapping
    @RequestMapping(value = "/category/id/{categoryId}/artists", method = RequestMethod.GET)
    public ResponseEntity<SuccessResponse> getArtistsByCategoryId(@PathVariable Long categoryId) {
        List<Artist> artists = artistCategoryService.getArtistsByCategoryId(categoryId);
        List<SearchedArtistOutput> output = new ArrayList<>();
        for (Artist artist : artists) {
            output.add(new SearchedArtistOutput(artist));
        }
        return new ResponseEntity<>(new SuccessResponse(output), HttpStatus.OK);
    }
    @GetMapping
    @RequestMapping(value = "/category/slug/{categorySlug}/artists", method = RequestMethod.GET)
    public ResponseEntity<SuccessResponse> getArtistsByCategorySlug(@PathVariable String categorySlug) {
        List<Artist> artists = artistCategoryService.getArtistsByCategorySlug(categorySlug);
        List<SearchedArtistOutput> output = new ArrayList<>();
        for (Artist artist : artists) {
            output.add(new SearchedArtistOutput(artist));
        }
        return new ResponseEntity<>(new SuccessResponse(output), HttpStatus.OK);
    }
}
