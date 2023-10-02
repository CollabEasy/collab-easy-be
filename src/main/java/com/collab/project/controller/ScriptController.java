package com.collab.project.controller;

import com.collab.project.model.response.SuccessResponse;
import com.collab.project.repositories.ArtistRepository;
import com.collab.project.service.AnalyticsService;
import com.collab.project.service.impl.ScriptServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
import java.util.Map;

@RestController
@RequestMapping(value = "/admin/v1/script")
public class ScriptController {

    @Autowired
    ScriptServiceImpl scriptService;

    @PostMapping
    @RequestMapping(value = "/backfillProfileCompletion", method = RequestMethod.POST)
    public ResponseEntity<SuccessResponse> runLogic() {
        scriptService.updateProfileCompleteStatus();
        return new ResponseEntity<>(new SuccessResponse(), HttpStatus.OK);
    }

    @GetMapping
    @RequestMapping(value = "/backfillReferralCodes", method = RequestMethod.GET)
    public ResponseEntity<SuccessResponse> backfillReferralCodes() throws NoSuchAlgorithmException {
        scriptService.backfillReferralCodes();
        return new ResponseEntity<>(new SuccessResponse(), HttpStatus.OK);
    }

    @PostMapping
    @RequestMapping(value = "/backfillContestSubmissionPoints", method = RequestMethod.POST)
    public ResponseEntity<SuccessResponse> backfillContestSubmissionPoints() throws JsonProcessingException {
        scriptService.backfillContestSubmissionPoints();
        return new ResponseEntity<>(new SuccessResponse(), HttpStatus.OK);
    }
}
