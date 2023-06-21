package com.collab.project.controller;

import com.collab.project.email.EmailService;
import com.collab.project.model.EmailNotifyInput;
import com.collab.project.model.contest.Contest;
import com.collab.project.model.inputs.ArtistInput;
import com.collab.project.model.response.SuccessResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(value = "/api/v1/notify")
public class EmailController {

    @Autowired
    EmailService emailService;

    @PostMapping
    @RequestMapping(value = "/all", method = RequestMethod.POST)
    public ResponseEntity<SuccessResponse> sendEmailToAllUsers() {
        return new ResponseEntity<>(new SuccessResponse(), HttpStatus.OK);
    }

    @PostMapping
    @RequestMapping(value = "/user", method = RequestMethod.POST)
    public ResponseEntity<SuccessResponse> getAllContests(@RequestBody EmailNotifyInput input) throws MessagingException, GeneralSecurityException, IOException {
        emailService.sendEmailFromString(input.getSubject(), input.getUserId(), input.getContent());
        return new ResponseEntity<>(new SuccessResponse(), HttpStatus.OK);
    }
}
