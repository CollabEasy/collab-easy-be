package com.collab.project.controller;

import com.collab.project.model.art.ArtCategory;
import com.collab.project.model.inputs.CategoryInput;
import com.collab.project.model.response.SuccessResponse;
import com.collab.project.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@CrossOrigin
@RestController
@RequestMapping(value = "/api/v1/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ResponseEntity<SuccessResponse> addCategory(@RequestBody CategoryInput categoryInput) {
        ArtCategory category = categoryService.addCategory(categoryInput);
        return new ResponseEntity<>(new SuccessResponse(category), HttpStatus.OK);
    }

    @GetMapping
    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public ResponseEntity<SuccessResponse> getAllCategories() {
        List<ArtCategory> arts = categoryService.getDefaultCategory();
        return new ResponseEntity<>(new SuccessResponse(arts), HttpStatus.OK);
    }

    @GetMapping
    @RequestMapping(value = "/slug/{categorySlug}", method = RequestMethod.GET)
    public ResponseEntity<SuccessResponse> getCategoryBySlug(@PathVariable String categorySlug) {
        ArtCategory category = categoryService.getCategoryBySlug(categorySlug);
        return new ResponseEntity<>(new SuccessResponse(category), HttpStatus.OK);
    }
}
