package com.collab.project.controller;

import com.collab.project.model.inputs.ArtistSocialProspectusInput;
import com.collab.project.model.response.SuccessResponse;
import com.collab.project.model.scratchpad.Scratchpad;
import com.collab.project.model.socialprospectus.ArtistSocialProspectus;
import com.collab.project.repositories.ArtistSocialProspectusRepository;
import com.collab.project.service.ArtistScratchpadService;
import com.collab.project.service.ArtistSocialProspectusService;
import com.collab.project.util.AuthUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(value = "/api/v1/artist/")
public class ArtistSocialProspectusController {

    @Autowired
    private ArtistSocialProspectusService artistSocialProspectusService;

    @PostMapping
    @RequestMapping(value = "/social-prospectus", method = RequestMethod.POST)
    public ResponseEntity<SuccessResponse> UpdateArtistSocialProspectus(@RequestBody List<ArtistSocialProspectusInput> updates) {
        //List<ArtistSocialProspectus> prospectus = ArtistSocialProspectusService.findByArtistId(AuthUtils.getArtistId());

        return new ResponseEntity<>(new SuccessResponse(updates), HttpStatus.OK);
    }

    @GetMapping
    @RequestMapping(value = "/scratchpad-prospectus", method = RequestMethod.GET)
    public ResponseEntity<SuccessResponse> getArtistSocialProspectus() {
        List<ArtistSocialProspectus> prospectus = artistSocialProspectusService.getSocialProspectByArtistId(AuthUtils.getArtistId());
        return new ResponseEntity<>(new SuccessResponse(prospectus), HttpStatus.OK);
    }
}
