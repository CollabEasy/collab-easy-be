package com.collab.project.controller;

import com.collab.project.model.artist.ArtistCategory;
import com.collab.project.model.collab.CollabRequest;
import com.collab.project.model.inputs.AcceptRequestInput;
import com.collab.project.model.inputs.CollabRequestInput;
import com.collab.project.model.inputs.RejectRequestInput;
import com.collab.project.model.response.SuccessResponse;
import com.collab.project.service.ArtistCategoryService;
import com.collab.project.service.CollabService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
// TODO : authenticate request from current user_id.
@RequestMapping(value = "/api/v1/collab", headers = "Accept=application/json")
public class CollabController {

    @Autowired
    private CollabService collabService;

    @PostMapping(value = "/request")
    public ResponseEntity<SuccessResponse> sendRequest(@RequestBody @Validated CollabRequestInput collabRequestInput) {
        CollabRequest collabRequest = collabService.sendRequest("1", collabRequestInput);
        return new ResponseEntity<>(new SuccessResponse(collabRequest), HttpStatus.OK);
    }

    @PostMapping(value = "/reject")
    public ResponseEntity<SuccessResponse> rejectRequest(@RequestBody @Validated RejectRequestInput rejectRequestInput) {
        CollabRequest collabRequest = collabService.rejectRequest("1", rejectRequestInput);
        return new ResponseEntity<>(new SuccessResponse(collabRequest), HttpStatus.OK);
    }

    @PostMapping(value = "/accept")
    public ResponseEntity<SuccessResponse> acceptRequest(@RequestBody @Validated AcceptRequestInput acceptRequestInput) {
        CollabRequest collabRequest = collabService.acceptRequest("1", acceptRequestInput);
        return new ResponseEntity<>(new SuccessResponse(collabRequest), HttpStatus.OK);
    }





}
