package com.collab.project.controller;

import com.collab.project.model.artist.ArtSample;
import com.collab.project.model.artist.Artist;
import com.collab.project.model.inputs.ArtistInput;
import com.collab.project.model.response.ArtistSampleResponse;
import com.collab.project.model.response.SuccessResponse;
import com.collab.project.repositories.ArtistRepository;
import com.collab.project.service.ArtistSampleService;
import com.collab.project.service.ArtistService;
import com.collab.project.util.AuthUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@CrossOrigin
@RequestMapping(method = RequestMethod.POST, value = "/api/v1/artist")
@RestController
public class ArtistController {

    @Autowired
    ArtistService artistService;

    @Autowired
    AuthUtils authUtils;

    @Autowired
    ArtistRepository artistRepository;

    @Autowired
    ArtistSampleService artistSampleService;

    @Autowired
    ObjectMapper objectMapper;


    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public ResponseEntity<?> update(@RequestBody ArtistInput input) {
        return new ResponseEntity(
            new SuccessResponse(artistService.updateArtist(input), "SUCCESS"), HttpStatus.OK);
    }


    @RequestMapping(value = "/login/delete", method = RequestMethod.POST)
    public ResponseEntity<?> delete(@RequestBody ArtistInput input) {
        artistService.delete(input);
        return new ResponseEntity<>(new SuccessResponse("Deleted", "SUCCESS"),
            HttpStatus.OK);
    }

    @RequestMapping(value = "/details", method = RequestMethod.GET)
    public ResponseEntity<?> getDetails() {
        return ResponseEntity.ok(artistService.getArtist());
    }

    @RequestMapping(value = "/profile", method = RequestMethod.GET)
    public ResponseEntity<?> getPublicProfile(@RequestBody ArtistInput artistInput) {
        Artist artist = artistRepository.findByArtistId(artistInput.getArtistId());
        List<ArtSample> samples = artistSampleService.getAllArtSamples(artistInput.getArtistId());
        Map<String, Object> map = objectMapper.convertValue(artist, Map.class);
        map.put("sample",new ArtistSampleResponse(artistInput.getArtistId(),
            samples));
        return ResponseEntity.ok(map);
    }
}
