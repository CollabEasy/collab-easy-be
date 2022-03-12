package com.collab.project.controller;

import com.collab.project.model.inputs.ArtistSocialProspectusInput;
import com.collab.project.model.response.SuccessResponse;
import com.collab.project.model.socialprospectus.ArtistSocialProspectus;
import com.collab.project.service.ArtistSocialProspectusService;
import com.collab.project.util.AuthUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.collab.project.helpers.Constants.FALLBACK_ID;

@CrossOrigin
@RestController
@RequestMapping(value = "/api/v1/artist/social-prospectus/")
public class ArtistSocialProspectusController {

    @Autowired
    private ArtistSocialProspectusService artistSocialProspectusService;

    @PostMapping
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ResponseEntity<SuccessResponse> UpdateArtistSocialProspectus(@RequestBody ArtistSocialProspectusInput artistSocialProspectusInput) {
        ArtistSocialProspectus prospectus = artistSocialProspectusService.addArtistSocialProspectus(artistSocialProspectusInput);
        return new ResponseEntity<>(new SuccessResponse(prospectus), HttpStatus.OK);
    }

    @GetMapping
    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public ResponseEntity<SuccessResponse> getArtistSocialProspectus() {
        List<ArtistSocialProspectus> prospectus = artistSocialProspectusService.getSocialProspectByArtistId(AuthUtils.getArtistId());
        return new ResponseEntity<>(new SuccessResponse(prospectus), HttpStatus.OK);
    }

    @PostMapping
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public ResponseEntity<SuccessResponse> deleteArtistSocialProspectus(@RequestBody Long platformId) {
        String artistId = AuthUtils.getArtistId();
        artistSocialProspectusService.deleteSocialProspectus(artistId, platformId);
        return new ResponseEntity<>(new SuccessResponse(), HttpStatus.OK);
    }
}
