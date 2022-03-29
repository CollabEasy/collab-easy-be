package com.collab.project.controller;

import com.collab.project.model.artist.Artist;
import com.collab.project.model.inputs.ArtistSocialProspectusInput;
import com.collab.project.model.response.SuccessResponse;
import com.collab.project.model.socialprospectus.ArtistSocialProspectus;
import com.collab.project.service.ArtistService;
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
    private ArtistService artistService;

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
    public ResponseEntity<SuccessResponse> getArtistSocialProspectus(@RequestParam(required = false) String handle) {
        Artist artist;
        if (handle == null || handle.equals("")) {
            artist = artistService.getArtistById(AuthUtils.getArtistId());
        } else {
            artist = artistService.getArtistBySlug(handle);
        }
        List<ArtistSocialProspectus> prospectus = artistSocialProspectusService.getSocialProspectByArtistId(artist.getArtistId());
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
