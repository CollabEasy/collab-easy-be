package com.collab.project.controller;

import com.collab.project.model.response.SuccessResponse;
import com.collab.project.model.socialprospectus.SocialPlatform;
import com.collab.project.service.SocialPlatformService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(value = "/api/v1/social-platform/")
public class SocialPlatformController {

    @Autowired
    private SocialPlatformService socialPlatformService;

    @GetMapping
    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public ResponseEntity<SuccessResponse> getAllSocialPlatforms() {
        System.out.println("Rabbal is here");
        List<SocialPlatform> platforms = socialPlatformService.getDefaultSocialPlatform();
        return new ResponseEntity<>(new SuccessResponse(platforms), HttpStatus.OK);
    }
}

