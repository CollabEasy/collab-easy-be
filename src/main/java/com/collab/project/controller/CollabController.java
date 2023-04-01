package com.collab.project.controller;

import com.collab.project.model.collab.CollabRequest;
import com.collab.project.model.collab.CollabRequestOutput;
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
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Positive;
import java.util.List;

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
        CollabRequest collabRequest = collabService.sendRequest(AuthUtils.getArtistId(), collabRequestInput);
        return new ResponseEntity<>(new SuccessResponse(collabRequest), HttpStatus.OK);
    }

    @PostMapping(value = "/request/update")
    public ResponseEntity<SuccessResponse> updateRequest(@RequestBody @Validated CollabRequest collabRequestInput) {
        CollabRequest collabRequest = collabService.updateRequest(AuthUtils.getArtistId(), collabRequestInput);
        return new ResponseEntity<>(new SuccessResponse(collabRequest), HttpStatus.OK);
    }

    @PostMapping(value = "/cancel/request/{requestId}")
    public ResponseEntity<SuccessResponse> cancelRequest(@PathVariable String requestId) {
        collabService.cancelRequest(AuthUtils.getArtistId(), requestId);
        return new ResponseEntity<>(new SuccessResponse(), HttpStatus.OK);
    }

    @PostMapping(value = "/reject/request/{requestId}")
    public ResponseEntity<SuccessResponse> rejectRequest(@PathVariable("requestId") String rejectRequestId) {
        CollabRequest collabRequest = collabService.rejectRequest(AuthUtils.getArtistId(), rejectRequestId);
        return new ResponseEntity<>(new SuccessResponse(collabRequest), HttpStatus.OK);
    }

    @PostMapping(value = "/accept/request/{requestId}")
    public ResponseEntity<SuccessResponse> acceptRequest(@PathVariable("requestId") String acceptRequestId) {
        CollabRequest collabRequest = collabService.acceptRequest(AuthUtils.getArtistId(), acceptRequestId);
        return new ResponseEntity<>(new SuccessResponse(collabRequest), HttpStatus.OK);
    }

    @PostMapping(value = "/complete/request/{requestId}")
    public ResponseEntity<SuccessResponse> completeRequest(@PathVariable("requestId") String completeRequestId) {
        CollabRequest collabRequest = collabService.completeRequest(AuthUtils.getArtistId(), completeRequestId);
        return new ResponseEntity<>(new SuccessResponse(collabRequest), HttpStatus.OK);
    }

    @PostMapping(value = "/search")
    public ResponseEntity<SuccessResponse> collabRequestsSearch(@RequestBody @Validated CollabRequestSearch collabRequestSearch) {
       CollabRequestOutput collabRequestOutput = collabService.collabRequestsSearch(AuthUtils.getArtistId(), collabRequestSearch);
       return new ResponseEntity<>(new SuccessResponse(collabRequestOutput), HttpStatus.OK);
    }


}
