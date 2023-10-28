package com.collab.project.controller;

import com.collab.project.model.inputs.ProposalAnswerInput;
import com.collab.project.model.inputs.ProposalInput;
import com.collab.project.model.inputs.ProposalQuestionInput;
import com.collab.project.model.proposal.Proposal;
import com.collab.project.model.proposal.ProposalInterest;
import com.collab.project.model.proposal.ProposalQuestion;
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
        Proposal proposal = proposalService.createProposal(artistId, proposalInput);
        return new ResponseEntity<>(new SuccessResponse(proposal), HttpStatus.OK);
    }

    @PostMapping
    @RequestMapping(value = "/{proposalId}/update", method = RequestMethod.POST)
    public ResponseEntity<SuccessResponse> updateProposal(@RequestParam String proposalId, @RequestBody ProposalInput proposalInput) throws JsonProcessingException {
        String artistId = AuthUtils.getArtistId();
        Proposal proposal = proposalService.updateProposal(artistId, proposalId, proposalInput);
        return new ResponseEntity<>(new SuccessResponse(proposal), HttpStatus.OK);
    }

    @GetMapping
    @RequestMapping(value = "/{proposalId}", method = RequestMethod.GET)
    public ResponseEntity<SuccessResponse> getProposal(@RequestParam String proposalId) throws JsonProcessingException {
        String artistId = AuthUtils.getArtistId();
        Proposal proposal = proposalService.getProposal(proposalId);
        return new ResponseEntity<>(new SuccessResponse(proposal), HttpStatus.OK);
    }

    @GetMapping
    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public ResponseEntity<SuccessResponse> getAllProposals(@RequestBody Map<String, List<Long>> categoriesMap) throws JsonProcessingException {
        String artistId = AuthUtils.getArtistId();
        List<Proposal> proposals = proposalService.getProposalByCategories(categoriesMap.getOrDefault("categories", new ArrayList<>()));
        return new ResponseEntity<>(new SuccessResponse(proposals), HttpStatus.OK);
    }

    @GetMapping
    @RequestMapping(value = "/my/get", method = RequestMethod.GET)
    public ResponseEntity<SuccessResponse> getMyProposals() throws JsonProcessingException {
        String artistId = AuthUtils.getArtistId();
        List<Proposal> proposals = proposalService.getArtistProposals(artistId);
        return new ResponseEntity<>(new SuccessResponse(proposals), HttpStatus.OK);
    }

    @GetMapping
    @RequestMapping(value = "/{proposalId}/questions/get", method = RequestMethod.GET)
    public ResponseEntity<SuccessResponse> getQuestions(@RequestParam String proposalId) throws JsonProcessingException {
        String artistId = AuthUtils.getArtistId();
        List<ProposalQuestion> questions = proposalService.getQuestionsOnProposals(proposalId);
        return new ResponseEntity<>(new SuccessResponse(questions), HttpStatus.OK);
    }


    @PostMapping
    @RequestMapping(value = "/{proposalId}/ask", method = RequestMethod.POST)
    public ResponseEntity<SuccessResponse> askQuestion(@RequestParam String proposalId, @RequestBody ProposalQuestionInput proposalQuestion) throws JsonProcessingException {
        String artistId = AuthUtils.getArtistId();
        List<ProposalQuestion> questions = proposalService.askQuestion(artistId, proposalId, proposalQuestion);
        return new ResponseEntity<>(new SuccessResponse(questions), HttpStatus.OK);
    }

    @PostMapping
    @RequestMapping(value = "/{proposalId}/answer", method = RequestMethod.POST)
    public ResponseEntity<SuccessResponse> answerQuestion(@RequestParam String proposalId, @RequestBody ProposalAnswerInput proposalQuestion) throws JsonProcessingException {
        String artistId = AuthUtils.getArtistId();
        List<ProposalQuestion> questions = proposalService.answerQuestion(artistId, proposalId, proposalQuestion);
        return new ResponseEntity<>(new SuccessResponse(questions), HttpStatus.OK);
    }

    @GetMapping
    @RequestMapping(value = "/{proposalId}/interests/get", method = RequestMethod.GET)
    public ResponseEntity<SuccessResponse> getAllInterests(@RequestParam String proposalId) throws JsonProcessingException {
        String artistId = AuthUtils.getArtistId();
        List<ProposalInterest> interests = proposalService.getAllInterests(proposalId);
        return new ResponseEntity<>(new SuccessResponse(), HttpStatus.OK);
    }

    @PostMapping
    @RequestMapping(value = "/{proposalId}/show_interest", method = RequestMethod.POST)
    public ResponseEntity<SuccessResponse> showInterest(@RequestParam String proposalId, Map<String, String> message) throws JsonProcessingException {
        String artistId = AuthUtils.getArtistId();
        ProposalInterest interests = proposalService.updateInterest(artistId, proposalId, true, message.get("message"));
        return new ResponseEntity<>(new SuccessResponse(interests), HttpStatus.OK);
    }

    @PostMapping
    @RequestMapping(value = "/{proposalId}/remove_interest", method = RequestMethod.POST)
    public ResponseEntity<SuccessResponse> removeInterest(@RequestParam String proposalId) throws JsonProcessingException {
        String artistId = AuthUtils.getArtistId();
        ProposalInterest interests = proposalService.updateInterest(artistId, proposalId, false, null);
        return new ResponseEntity<>(new SuccessResponse(interests), HttpStatus.OK);
    }

    @PostMapping
    @RequestMapping(value = "/{proposalId}/accept", method = RequestMethod.POST)
    public ResponseEntity<SuccessResponse> acceptInterest(@RequestParam String proposalId, Map<String, String> user) throws JsonProcessingException {
        String artistId = AuthUtils.getArtistId();
        List<ProposalInterest> interests = proposalService.acceptInterest(artistId, proposalId, new ArrayList<>(user.values()));
        return new ResponseEntity<>(new SuccessResponse(), HttpStatus.OK);
    }
}
