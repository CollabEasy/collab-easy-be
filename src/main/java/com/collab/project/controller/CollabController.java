package com.collab.project.controller;

import com.collab.project.model.collab.CollabEligibilityOutput;
import com.collab.project.model.collab.CollabRequest;
import com.collab.project.model.collab.CollabRequestOutput;
import com.collab.project.model.collab.CollabRequestResponse;
import com.collab.project.model.enums.Enums;
import com.collab.project.model.inputs.CollabRequestInput;
import com.collab.project.model.inputs.CollabRequestSearch;
import com.collab.project.model.response.SuccessResponse;
import com.collab.project.service.CollabService;
import com.collab.project.util.AuthUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Positive;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
// TODO : authenticate request from current user_id.
@Validated
@RequestMapping(value = "/api/v1/collab", headers = "Accept=application/json")
public class CollabController {

    @Autowired
    private CollabService collabService;

    @PostMapping(value = "/request")
    public ResponseEntity<SuccessResponse> sendRequest(
            @RequestBody @Validated CollabRequestInput collabRequestInput
    ) throws JsonProcessingException {
        CollabRequestResponse collabRequest = collabService.sendRequest(AuthUtils.getArtistId(), collabRequestInput);
        return new ResponseEntity<>(new SuccessResponse(collabRequest), HttpStatus.OK);
    }

    @PostMapping(value = "/request/update")
    public ResponseEntity<SuccessResponse> updateRequest(@RequestBody @Validated CollabRequest collabRequestInput) {
        CollabRequestResponse collabRequest = collabService.updateRequest(AuthUtils.getArtistId(), collabRequestInput);
        return new ResponseEntity<>(new SuccessResponse(collabRequest), HttpStatus.OK);
    }

    @PostMapping(value = "/cancel/request/{requestId}")
    public ResponseEntity<SuccessResponse> cancelRequest(@PathVariable String requestId) {
        collabService.cancelRequest(AuthUtils.getArtistId(), requestId);
        return new ResponseEntity<>(new SuccessResponse(), HttpStatus.OK);
    }

    @PostMapping(value = "/reject/request/{requestId}")
    public ResponseEntity<SuccessResponse> rejectRequest(@PathVariable("requestId") String rejectRequestId) {
        CollabRequestResponse collabRequest = collabService.rejectRequest(AuthUtils.getArtistId(), rejectRequestId);
        return new ResponseEntity<>(new SuccessResponse(collabRequest), HttpStatus.OK);
    }

    @PostMapping(value = "/accept/request/{requestId}")
    public ResponseEntity<SuccessResponse> acceptRequest(@PathVariable("requestId") String acceptRequestId) {
        CollabRequestResponse collabRequest = collabService.acceptRequest(AuthUtils.getArtistId(), acceptRequestId);
        return new ResponseEntity<>(new SuccessResponse(collabRequest), HttpStatus.OK);
    }

    @PostMapping(value = "/complete/request/{requestId}")
    public ResponseEntity<SuccessResponse> completeRequest(@PathVariable("requestId") String completeRequestId) {
        CollabRequestResponse collabRequest = collabService.completeRequest(AuthUtils.getArtistId(), completeRequestId);
        return new ResponseEntity<>(new SuccessResponse(collabRequest), HttpStatus.OK);
    }

    @PostMapping(value = "/search")
    public ResponseEntity<SuccessResponse> collabRequestsSearch(@RequestBody @Validated CollabRequestSearch collabRequestSearch) {
       CollabRequestOutput collabRequestOutput = collabService.collabRequestsSearch(AuthUtils.getArtistId(), collabRequestSearch);
       return new ResponseEntity<>(new SuccessResponse(collabRequestOutput), HttpStatus.OK);
    }

    @PostMapping(value = "/create/eligible")
    public ResponseEntity<SuccessResponse> canCreateCollabRequest(@RequestBody Map<String, String> userMap) {
        String user1Slug = userMap.getOrDefault("user", null);
        String user2 = AuthUtils.getArtistId();
        CollabEligibilityOutput output = collabService.canCreateNewCollabRequest(user2, user1Slug);
        return new ResponseEntity<>(new SuccessResponse(output), HttpStatus.OK);
    }

    @GetMapping(value = "/getByDate")
    public ResponseEntity<SuccessResponse> getCollabsByDate(@RequestParam String all) {
        String artistId = AuthUtils.getArtistId();
        boolean fetchAll = (all != null && all.equalsIgnoreCase("true"));
        Map<String, List<CollabRequestResponse>> collabs = collabService.fetchCollabsByDate(artistId, fetchAll);
        return new ResponseEntity<>(new SuccessResponse(collabs), HttpStatus.OK);
    }
}
