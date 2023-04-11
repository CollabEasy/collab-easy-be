package com.collab.project.controller;

import com.collab.project.model.contest.Contest;
import com.collab.project.model.inputs.ContestInput;
import com.collab.project.model.response.SuccessResponse;
import com.collab.project.service.impl.ContestServiceImpl;
import com.collab.project.util.AuthUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(value = "/api/v1/contest/")
public class ContestController {

    @Autowired
    private ContestServiceImpl contestService;

    @GetMapping
    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public ResponseEntity<SuccessResponse> getAllContests() {
        List<Contest> contests = contestService.getAllContests();
        return new ResponseEntity<>(new SuccessResponse(contests), HttpStatus.OK);
    }

    @GetMapping
    @RequestMapping(value = "/{contestSlug}", method = RequestMethod.GET)
    public ResponseEntity<SuccessResponse> getContest(@PathVariable String contestSlug) {
        Contest contest = contestService.getContestBySlug(contestSlug);
        return new ResponseEntity<>(new SuccessResponse(contest), HttpStatus.OK);
    }

    @PostMapping
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ResponseEntity<SuccessResponse> AddContest(@RequestBody ContestInput contestInput) {
        Contest contest = contestService.addContest(contestInput);
        return new ResponseEntity<>(new SuccessResponse(contest), HttpStatus.OK);
    }

    @PostMapping
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public ResponseEntity<SuccessResponse> UpdateContest(@RequestBody ContestInput contestInput) {
        Contest contest = contestService.addContest(contestInput);
        return new ResponseEntity<>(new SuccessResponse(contest), HttpStatus.OK);
    }
}

