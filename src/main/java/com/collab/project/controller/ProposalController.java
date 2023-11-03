package com.collab.project.controller;

import com.collab.project.model.inputs.ProposalAnswerInput;
import com.collab.project.model.inputs.ProposalInput;
import com.collab.project.model.inputs.ProposalQuestionInput;
import com.collab.project.model.proposal.Proposal;
import com.collab.project.model.proposal.ProposalInterest;
import com.collab.project.model.proposal.ProposalQuestion;
import com.collab.project.model.proposal.ProposalResponse;
import com.collab.project.model.response.SuccessResponse;
import com.collab.project.model.rewards.ReferralCodeResponse;
import com.collab.project.service.ProposalService;
import com.collab.project.util.AuthUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/v1/proposal")
public class ProposalController {

    @Autowired
    ProposalService proposalService;

    @PostMapping
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResponseEntity<SuccessResponse> createProposal(@RequestBody ProposalInput proposalInput) throws JsonProcessingException {
        String artistId = AuthUtils.getArtistId();
        ProposalResponse proposal = proposalService.createProposal(artistId, proposalInput);
        return new ResponseEntity<>(new SuccessResponse(proposal), HttpStatus.OK);
    }

    @PostMapping
    @RequestMapping(value = "/{proposalId}/update", method = RequestMethod.POST)
    public ResponseEntity<SuccessResponse> updateProposal(@PathVariable String proposalId, @RequestBody ProposalInput proposalInput) throws JsonProcessingException {
        String artistId = AuthUtils.getArtistId();
        Proposal proposal = proposalService.updateProposal(artistId, proposalId, proposalInput);
        return new ResponseEntity<>(new SuccessResponse(proposal), HttpStatus.OK);
    }

    @GetMapping
    @RequestMapping(value = "/{proposalId}", method = RequestMethod.GET)
    public ResponseEntity<SuccessResponse> getProposal(@PathVariable String proposalId) throws JsonProcessingException {
        ProposalResponse proposal = proposalService.getProposal(proposalId);
        return new ResponseEntity<>(new SuccessResponse(proposal), HttpStatus.OK);
    }

    @PostMapping
    @RequestMapping(value = "/get", method = RequestMethod.POST)
    public ResponseEntity<SuccessResponse> getAllProposals(@RequestBody Map<String, List<Long>> categoriesMap) throws JsonProcessingException {
        List<ProposalResponse> proposals = proposalService.getProposalByCategories(categoriesMap.getOrDefault("categories", new ArrayList<>()));
        return new ResponseEntity<>(new SuccessResponse(proposals), HttpStatus.OK);
    }

    @GetMapping
    @RequestMapping(value = "/{artistSlug}/get", method = RequestMethod.GET)
    public ResponseEntity<SuccessResponse> getMyProposals(@PathVariable String artistSlug) throws JsonProcessingException {
        List<ProposalResponse> proposals = proposalService.getArtistProposals(artistSlug);
        return new ResponseEntity<>(new SuccessResponse(proposals), HttpStatus.OK);
    }

    @GetMapping
    @RequestMapping(value = "/{proposalId}/questions/get", method = RequestMethod.GET)
    public ResponseEntity<SuccessResponse> getQuestions(@PathVariable String proposalId) throws JsonProcessingException {
        List<ProposalQuestion> questions = proposalService.getQuestionsOnProposals(proposalId);
        return new ResponseEntity<>(new SuccessResponse(questions), HttpStatus.OK);
    }


    @PostMapping
    @RequestMapping(value = "/{proposalId}/ask", method = RequestMethod.POST)
    public ResponseEntity<SuccessResponse> askQuestion(@PathVariable String proposalId, @RequestBody ProposalQuestionInput proposalQuestion) throws JsonProcessingException {
        String artistId = AuthUtils.getArtistId();
        List<ProposalQuestion> questions = proposalService.askQuestion(artistId, proposalId, proposalQuestion);
        return new ResponseEntity<>(new SuccessResponse(questions), HttpStatus.OK);
    }

    @PostMapping
    @RequestMapping(value = "/{proposalId}/answer", method = RequestMethod.POST)
    public ResponseEntity<SuccessResponse> answerQuestion(@PathVariable String proposalId, @RequestBody ProposalAnswerInput proposalQuestion) throws JsonProcessingException {
        String artistId = AuthUtils.getArtistId();
        List<ProposalQuestion> questions = proposalService.answerQuestion(artistId, proposalId, proposalQuestion);
        return new ResponseEntity<>(new SuccessResponse(questions), HttpStatus.OK);
    }

    @GetMapping
    @RequestMapping(value = "/{proposalId}/interests/get", method = RequestMethod.GET)
    public ResponseEntity<SuccessResponse> getAllInterests(@PathVariable String proposalId) throws JsonProcessingException {
        List<ProposalInterest> interests = proposalService.getAllInterests(proposalId);
        return new ResponseEntity<>(new SuccessResponse(interests), HttpStatus.OK);
    }

    @PostMapping
    @RequestMapping(value = "/{proposalId}/show_interest", method = RequestMethod.POST)
    public ResponseEntity<SuccessResponse> showInterest(@PathVariable String proposalId, Map<String, String> message) throws JsonProcessingException {
        String artistId = AuthUtils.getArtistId();
        ProposalInterest interests = proposalService.updateInterest(artistId, proposalId, true, message.get("message"));
        return new ResponseEntity<>(new SuccessResponse(interests), HttpStatus.OK);
    }

    @PostMapping
    @RequestMapping(value = "/{proposalId}/remove_interest", method = RequestMethod.POST)
    public ResponseEntity<SuccessResponse> removeInterest(@PathVariable String proposalId) throws JsonProcessingException {
        String artistId = AuthUtils.getArtistId();
        ProposalInterest interests = proposalService.updateInterest(artistId, proposalId, false, null);
        return new ResponseEntity<>(new SuccessResponse(interests), HttpStatus.OK);
    }

    @PostMapping
    @RequestMapping(value = "/{proposalId}/accept", method = RequestMethod.POST)
    public ResponseEntity<SuccessResponse> acceptInterest(@PathVariable String proposalId, @RequestBody Map<String, String> user) throws JsonProcessingException {
        String artistId = AuthUtils.getArtistId();
        List<ProposalInterest> interests = proposalService.acceptInterest(artistId, proposalId, new ArrayList<>(user.values()));
        return new ResponseEntity<>(new SuccessResponse(interests), HttpStatus.OK);
    }

    @PostMapping
    @RequestMapping(value = "/{proposalId}/reject", method = RequestMethod.POST)
    public ResponseEntity<SuccessResponse> rejectInterest(@PathVariable String proposalId, @RequestBody Map<String, String> user) throws JsonProcessingException {
        String artistId = AuthUtils.getArtistId();
        List<ProposalInterest> interests = proposalService.rejectInterest(artistId, proposalId, new ArrayList<>(user.values()));
        return new ResponseEntity<>(new SuccessResponse(interests), HttpStatus.OK);
    }
}
