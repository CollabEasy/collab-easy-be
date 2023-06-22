package com.collab.project.controller;

import com.collab.project.email.EmailService;
import com.collab.project.helpers.Constants;
import com.collab.project.model.email.EmailNotifyInput;
import com.collab.project.model.response.SuccessResponse;
import com.collab.project.service.EmailHistoryService;
import com.collab.project.util.AuthUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.io.IOException;
import java.security.GeneralSecurityException;

@CrossOrigin
@RestController
@RequestMapping(value = "/api/v1")
public class EmailController {

    @Autowired
    EmailService emailService;

    @Autowired
    EmailHistoryService emailHistoryService;

    @PostMapping
    @RequestMapping(value = "/notify/all", method = RequestMethod.POST)
    public ResponseEntity<SuccessResponse> sendEmailToAllUsers(@RequestBody EmailNotifyInput input) {
        emailService.sendEmailToAllUsersFromString(input.getSubject(), input.getContent());
        return new ResponseEntity<>(new SuccessResponse(), HttpStatus.OK);
    }

    @PostMapping
    @RequestMapping(value = "/notify/user", method = RequestMethod.POST)
    public ResponseEntity<SuccessResponse> sendEmailToOneUser(@RequestBody EmailNotifyInput input) throws MessagingException,
            GeneralSecurityException, IOException {
        String artistId = AuthUtils.getArtistId();
        emailService.sendEmailFromString(input.getSubject(), artistId, null, input.getContent());
        return new ResponseEntity<>(new SuccessResponse(), HttpStatus.OK);
    }

    @PostMapping
    @RequestMapping(value = "/notify/group/{group_enum}", method = RequestMethod.POST)
    public ResponseEntity<SuccessResponse> sendEmailToOneUser(@PathVariable String group_enum, @RequestBody EmailNotifyInput input) throws MessagingException,
            GeneralSecurityException, IOException {
        if (Constants.EmailGroups.contains(group_enum)) {
            emailService.sendEmailToGroup(group_enum, input.getSubject(), input.getContent());
        }
        return new ResponseEntity<>(new SuccessResponse(), HttpStatus.OK);
    }

    @GetMapping
    @RequestMapping(value = "/email/enums/all", method = RequestMethod.GET) 
    public ResponseEntity<SuccessResponse> fetchAllEnumDetails() {
        return new ResponseEntity<>(new SuccessResponse(emailHistoryService.getAllEmailEnumsHistory()), HttpStatus.OK);
    }
}
