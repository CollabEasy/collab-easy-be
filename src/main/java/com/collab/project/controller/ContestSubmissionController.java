package com.collab.project.controller;

import com.collab.project.model.contest.ContestSubmission;
import com.collab.project.model.contest.ContestSubmissionResponse;
import com.collab.project.model.contest.ContestSubmissionVote;
import com.collab.project.model.inputs.ContestSubmissionInput;
import com.collab.project.model.inputs.ContestSubmissionVoteInput;
import com.collab.project.model.response.SuccessResponse;
import com.collab.project.service.impl.ContestSubmissionServiceImpl;
import com.collab.project.service.impl.ContestSubmissionVoteServiceImpl;
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

@CrossOrigin
@RestController
@RequestMapping(value = "/api/v1/contest/submission/")
public class ContestSubmissionController {

    @Autowired
    private ContestSubmissionServiceImpl contestSubmissionService;

    @Autowired
    private ContestSubmissionVoteServiceImpl contestSubmissionVoteService;

    @GetMapping
    @RequestMapping(value = "{contestSlug}/all", method = RequestMethod.GET)
    public ResponseEntity<SuccessResponse> getContestSubmissions(@PathVariable String contestSlug) {
        List<ContestSubmissionResponse> contestSubmissions = contestSubmissionService.getContestSubmissions(contestSlug);
        return new ResponseEntity<>(new SuccessResponse(contestSubmissions), HttpStatus.OK);
    }

    @GetMapping
    @RequestMapping(value = "{contestSlug}/entry", method = RequestMethod.GET)
    public ResponseEntity<SuccessResponse> getContestSubmission(@PathVariable String contestSlug) {
        List<ContestSubmission> contestSubmissions = contestSubmissionService.getContestSubmission(contestSlug,
                AuthUtils.getArtistId());
        return new ResponseEntity<>(new SuccessResponse(contestSubmissions), HttpStatus.OK);
    }

    @PostMapping
    @RequestMapping(value = "add/entry", method = RequestMethod.POST)
    public ResponseEntity<SuccessResponse> AddContestSubmission(@RequestBody ContestSubmissionInput contestSubmissionInput) {
        ContestSubmission contestSubmission = contestSubmissionService.addContestSubmission(contestSubmissionInput);
        return new ResponseEntity<>(new SuccessResponse(contestSubmission), HttpStatus.OK);
    }

    @RequestMapping(value = "upload/{contestSlug}/artwork", method = RequestMethod.POST, consumes =
            {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<SuccessResponse> AddContestArtWork(@RequestPart MultipartFile filename,
                                                             @RequestPart String filetype,
                                                             @RequestPart String description,
                                                             @PathVariable String contestSlug) throws IOException,
            NoSuchAlgorithmException {
        ContestSubmission submission = contestSubmissionService.addArtwork(AuthUtils.getArtistId(), filename, filetype,
                description,
                contestSlug);
        return new ResponseEntity<>(new SuccessResponse(submission), HttpStatus.OK);
    }

    @RequestMapping(value = "/upvote", method = RequestMethod.POST)
    public ResponseEntity<SuccessResponse> UpvoteSubmission(@RequestBody ContestSubmissionVoteInput upvoteSubmission) {
        ContestSubmissionVote vote =
                contestSubmissionVoteService.updateContestSubmissionVote(AuthUtils.getArtistId(), upvoteSubmission.getSubmissionId(),
                        upvoteSubmission.getContestSlug());
        return new ResponseEntity<>(new SuccessResponse(vote), HttpStatus.OK);
    }

    @GetMapping
    @RequestMapping(value = "/votes/{contestSlug}", method = RequestMethod.GET)
    public ResponseEntity<SuccessResponse> GetSubmissionVote(@PathVariable String contestSlug) {
        List<ContestSubmissionVote> votes = contestSubmissionVoteService.getContestSubmissionVote(contestSlug);
        return new ResponseEntity<>(new SuccessResponse(votes), HttpStatus.OK);
    }

    @GetMapping
    @RequestMapping(value = "/artist/vote/{contestSlug}", method = RequestMethod.GET)
    public ResponseEntity<SuccessResponse> GetSubmissionVoteByArtist(@PathVariable String contestSlug) {
        List<ContestSubmissionVote> votes = contestSubmissionVoteService.getContestSubmissionVoteByArtist(contestSlug
                , AuthUtils.getArtistId());
        return new ResponseEntity<>(new SuccessResponse(votes), HttpStatus.OK);
    }
}

