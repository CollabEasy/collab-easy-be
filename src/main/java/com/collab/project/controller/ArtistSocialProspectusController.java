package com.collab.project.controller;

import com.collab.project.model.art.ArtInfo;
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

import static com.collab.project.helpers.Constants.FALLBACK_ID;

@CrossOrigin
@RestController
@RequestMapping(value = "/api/v1/artist/social-prospectus/")
public class ArtistSocialProspectusController {

    @Autowired
    private ArtistSocialProspectusService artistSocialProspectusService;

    @PostMapping
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public ResponseEntity<SuccessResponse> UpdateArtistSocialProspectus(@RequestBody List<ArtistSocialProspectusInput> updates) {
        System.out.println("Rabbal is here trying to post");
//        List<ArtistSocialProspectus> all_enteries = artistSocialProspectusService.getSocialProspectByArtistId(AuthUtils.getArtistId());

        // more logic yet it be done here. This is basic and blindly saving data in backend.
        for (ArtistSocialProspectusInput update : updates) {
            ArtistSocialProspectus entry = new ArtistSocialProspectus(FALLBACK_ID, AuthUtils.getArtistId(),
                    update.getSocialPlatformId(), update.getHandle(), update.getDescription());
            artistSocialProspectusService.createArtistSocialProspectus(entry);
        }
        return new ResponseEntity<>(new SuccessResponse(updates), HttpStatus.OK);
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
