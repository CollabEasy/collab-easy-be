package com.collab.project.controller;

import com.collab.project.model.response.SuccessResponse;
import com.collab.project.model.scratchpad.Scratchpad;
import com.collab.project.service.ArtistScratchpadService;
import com.collab.project.util.AuthUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping(value = "/api/v1/artist/")
public class ArtistScratchpadController {

    @Autowired
    private ArtistScratchpadService artistScratchpadService;

    @PostMapping
    @RequestMapping(value = "/scratchpad", method = RequestMethod.POST)
    public ResponseEntity<SuccessResponse> AddArtistScratchpad(@RequestBody String content) {
        Scratchpad scratchpad = artistScratchpadService.addScratchpad(AuthUtils.getArtistId(), content);
        return new ResponseEntity<>(new SuccessResponse(scratchpad), HttpStatus.OK);
    }

    @GetMapping
    @RequestMapping(value = "/scratchpad", method = RequestMethod.GET)
    public ResponseEntity<SuccessResponse> getArtistScratchpad() {
        Scratchpad scratchpad = artistScratchpadService.getScratchpadByArtistId(AuthUtils.getArtistId());
        return new ResponseEntity<>(new SuccessResponse(scratchpad), HttpStatus.OK);
    }
}
