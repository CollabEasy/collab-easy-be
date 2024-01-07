package com.collab.project.controller;

import com.collab.project.model.analytics.UserAnalytics;
import com.collab.project.model.response.SearchResponse;
import com.collab.project.model.response.SuccessResponse;
import com.collab.project.service.AnalyticsService;
import com.collab.project.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/admin/v1/analytics")
public class AnalyticsController {

    @Autowired
    AnalyticsService analyticsService;

    @GetMapping
    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public ResponseEntity<SuccessResponse> getSearchResults(@RequestParam("start_date") String startDate, @RequestParam("end_date") String endDate) {
        UserAnalytics userAnalytics = analyticsService.getUsersJoinedCount(startDate, endDate);
        SuccessResponse successResponse = new SuccessResponse(userAnalytics);
        return new ResponseEntity<>(successResponse, HttpStatus.OK);
    }

    @GetMapping
    @RequestMapping(value = "/users/byCountry", method = RequestMethod.GET)
    public ResponseEntity<SuccessResponse> getSearchResultsByCountry() {
        Map<String, Integer> result = analyticsService.getCountryLevelArtists();
        SuccessResponse successResponse = new SuccessResponse(result);
        return new ResponseEntity<>(successResponse, HttpStatus.OK);
    }
}
