package com.collab.project.controller;

import com.collab.project.model.artist.ArtSample;
import com.collab.project.model.response.ArtistSampleResponse;
import com.collab.project.model.response.SuccessResponse;
import com.collab.project.service.ArtistSampleService;
import com.collab.project.util.AuthUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/artist")
public class ArtistSampleController {

    @Autowired
    ArtistSampleService artistSampleService;

    @RequestMapping(value = "/sample/upload", method = RequestMethod.POST, consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<SuccessResponse> uploadArtSample(@RequestPart MultipartFile filename, @RequestPart String filetype, @RequestPart String caption) throws IOException, NoSuchAlgorithmException {
        artistSampleService.uploadFile(AuthUtils.getArtistId(), caption, filetype, filename);
        return new ResponseEntity<>(new SuccessResponse(), HttpStatus.OK);
    }

    @RequestMapping(value = "/{slug}/sample/list", method = RequestMethod.GET)
    public ResponseEntity<SuccessResponse> getArtistSamples(@PathVariable String slug) {
        List<ArtSample> samples = artistSampleService.getAllArtSamples(slug);
        ArtistSampleResponse response = new ArtistSampleResponse(AuthUtils.getArtistId(), samples);
        return new ResponseEntity<>(new SuccessResponse(response), HttpStatus.OK);
    }
}
