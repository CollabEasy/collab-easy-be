package com.collab.project.controller;

import com.collab.project.model.artist.ArtistCategory;
import com.collab.project.model.collab.CollabRequest;
import com.collab.project.model.inputs.AcceptRequestInput;
import com.collab.project.model.inputs.CollabRequestInput;
import com.collab.project.model.inputs.CollabRequestSearch;
import com.collab.project.model.inputs.RejectRequestInput;
import com.collab.project.model.response.SuccessResponse;
import com.collab.project.service.ArtistCategoryService;
import com.collab.project.service.CollabService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Positive;
import java.util.List;

@RestController
// TODO : authenticate request from current user_id.
@Validated
@RequestMapping(value = "/api/v1/collab", headers = "Accept=application/json")
public class CollabController {

    @Autowired
    private CollabService collabService;

    @PostMapping(value = "/request")
    public ResponseEntity<SuccessResponse> sendRequest(@RequestBody @Validated CollabRequestInput collabRequestInput) {
        CollabRequest collabRequest = collabService.sendRequest("1", collabRequestInput);
        return new ResponseEntity<>(new SuccessResponse(collabRequest), HttpStatus.OK);
    }

    @PostMapping(value = "/reject/requestId/{requestId}")
    public ResponseEntity<SuccessResponse> rejectRequest(
            @PathVariable("requestId") @Positive(message = "rejectRequestId should be greater than 0") long rejectRequestId) {
        CollabRequest collabRequest = collabService.rejectRequest("1", rejectRequestId);
        return new ResponseEntity<>(new SuccessResponse(collabRequest), HttpStatus.OK);
    }

    @PostMapping(value = "/accept/requestId/{requestId}")
    public ResponseEntity<SuccessResponse> acceptRequest(@PathVariable("requestId") @Positive(message = "rejectRequestId should be greater than 0")
                                                                     long acceptRequestId) {
        CollabRequest collabRequest = collabService.acceptRequest("1", acceptRequestId);
        return new ResponseEntity<>(new SuccessResponse(collabRequest), HttpStatus.OK);
    }

    @PostMapping(value = "/search")
    public ResponseEntity<SuccessResponse> collabRequestsSearch(@RequestBody @Validated CollabRequestSearch collabRequestSearch) {
       List<CollabRequest> collabRequest = collabService.collabRequestsSearch("1", collabRequestSearch);
       return new ResponseEntity<>(new SuccessResponse(collabRequest), HttpStatus.OK);
    }



}
