package com.collab.project.controller;


import com.collab.project.model.artist.Artist;
import com.collab.project.model.artist.ArtistCategory;
import com.collab.project.model.artist.SearchedArtistOutput;
import com.collab.project.model.inputs.ArtistCategoryInput;
import com.collab.project.model.inputs.ArtistScratchpadInput;
import com.collab.project.model.response.SuccessResponse;
import com.collab.project.model.scratchpad.Scratchpad;
import com.collab.project.service.ArtistScratchpadService;
import com.collab.project.util.AuthUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
@CrossOrigin
@RestController
@RequestMapping(value = "/api/v1/artist/")
public class ArtistScratchpadController {

    @Autowired
    private ArtistScratchpadService artistScratchpadService;

    @PostMapping
    @RequestMapping(value = "/scratchpad", method = RequestMethod.POST)
    public ResponseEntity<SuccessResponse> AddArtistScratchpad(@RequestBody ArtistScratchpadInput artistScratchpadInput) {
        Scratchpad scratchpad = artistScratchpadService.addScratchpad(AuthUtils.getArtistId(), artistScratchpadInput);
        return new ResponseEntity<>(new SuccessResponse(scratchpad), HttpStatus.OK);
    }

    @GetMapping
    @RequestMapping(value = "/scratchpad", method = RequestMethod.GET)
    public ResponseEntity<SuccessResponse> getArtistScratchpad() {
        Scratchpad scratchpad = artistScratchpadService.getScratchpadByArtist(AuthUtils.getArtistId());
        return new ResponseEntity<>(new SuccessResponse(scratchpad), HttpStatus.OK);
    }
}
