package com.collab.project.controller;

import com.collab.project.model.collab.CollabConversation;
import com.collab.project.model.inputs.CollabCommentInput;
import com.collab.project.model.response.SuccessResponse;
import com.collab.project.service.CollabConversationService;
import com.collab.project.util.AuthUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(value = "/api/v1/collab/conversation")
public class CollabConversationController {
    @Autowired
    private CollabConversationService collabConversationService;

    @PostMapping
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ResponseEntity<SuccessResponse> addComment(@RequestBody CollabCommentInput collabCommentInput) {
        CollabConversation comment = collabConversationService.addComment(
                AuthUtils.getArtistId(), collabCommentInput.getCollabId(), collabCommentInput.getContent());
        return new ResponseEntity<>(new SuccessResponse(comment), HttpStatus.OK);
    }

    @GetMapping
    @RequestMapping(value = "/{collabId}", method = RequestMethod.GET)
    public ResponseEntity<SuccessResponse> getAllComments(@PathVariable String collabId) {
        List<CollabConversation> comments = collabConversationService.getCommentsByCollabId(collabId);
        return new ResponseEntity<>(new SuccessResponse(comments), HttpStatus.OK);
    }
}
