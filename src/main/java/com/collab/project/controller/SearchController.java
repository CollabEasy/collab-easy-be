package com.collab.project.controller;

import com.collab.project.model.response.SearchResponse;
import com.collab.project.model.response.SuccessResponse;
import com.collab.project.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/search")
public class SearchController {

    @Autowired
    SearchService searchService;

    @GetMapping
    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<SuccessResponse> getSearchResults(@RequestParam("query") String queryStr) {
        List<SearchResponse> searchResponseList = searchService.getSearchResults(queryStr);
        SuccessResponse successResponse = new SuccessResponse(searchResponseList);
        return new ResponseEntity<>(successResponse, HttpStatus.OK);
    }
}
