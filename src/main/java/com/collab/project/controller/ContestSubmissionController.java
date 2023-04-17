package com.collab.project.controller;

import com.collab.project.model.artist.Artist;
import com.collab.project.model.contest.Contest;
import com.collab.project.model.contest.ContestSubmission;
import com.collab.project.model.inputs.ContestInput;
import com.collab.project.model.inputs.ContestSubmissionInput;
import com.collab.project.model.response.SuccessResponse;
import com.collab.project.service.impl.ContestServiceImpl;
import com.collab.project.service.impl.ContestSubmissionServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import com.collab.project.model.contest.Contest;
import com.collab.project.model.inputs.ContestInput;
import com.collab.project.model.response.SuccessResponse;
import com.collab.project.service.impl.ContestServiceImpl;
import com.collab.project.util.AuthUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping(value = "/api/v1/contest/submission/")
public class ContestSubmissionController {

    @Autowired
    private ContestSubmissionServiceImpl contestSubmissionService;

    @GetMapping
    @RequestMapping(value = "{contestSlug}/all", method = RequestMethod.GET)
    public ResponseEntity<SuccessResponse> getContestSubmissions(@PathVariable String contestSlug) {
        List<ContestSubmission> contestSubmissions = contestSubmissionService.getContestSubmissions(contestSlug);
        return new ResponseEntity<>(new SuccessResponse(contestSubmissions), HttpStatus.OK);
    }

    @GetMapping
    @RequestMapping(value = "{contestSlug}/entry", method = RequestMethod.GET)
    public ResponseEntity<SuccessResponse> getContestSubmission(@PathVariable String contestSlug) {
        List<ContestSubmission> contestSubmissions = contestSubmissionService.getContestSubmission(contestSlug, AuthUtils.getArtistId());
        return new ResponseEntity<>(new SuccessResponse(contestSubmissions), HttpStatus.OK);
    }

    @PostMapping
    @RequestMapping(value = "/add/entry", method = RequestMethod.POST)
    public ResponseEntity<SuccessResponse> AddContestSubmission(@RequestBody ContestSubmissionInput contestSubmissionInput) {
        ContestSubmission contestSubmission = contestSubmissionService.addContestSubmission(contestSubmissionInput);
        return new ResponseEntity<>(new SuccessResponse(contestSubmission), HttpStatus.OK);
    }

    @RequestMapping(value = "/upload/artwork", method = RequestMethod.POST, consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<SuccessResponse> AddContestArtWork(@RequestPart MultipartFile filename) throws IOException, NoSuchAlgorithmException {
        String url = contestSubmissionService.addArtwork(AuthUtils.getArtistId(), filename);
        return new ResponseEntity<>(new SuccessResponse(url), HttpStatus.OK);
    }
}

