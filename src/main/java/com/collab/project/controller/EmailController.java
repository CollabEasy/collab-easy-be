package com.collab.project.controller;

import com.collab.project.email.EmailService;
import com.collab.project.helpers.Constants;
import com.collab.project.model.artist.Artist;
import com.collab.project.model.email.EmailNotifyInput;
import com.collab.project.model.email.EmailNotifyInputSlug;
import com.collab.project.model.response.SuccessResponse;
import com.collab.project.repositories.ArtistRepository;
import com.collab.project.service.EmailHistoryService;
import com.collab.project.service.impl.ScriptServiceImpl;
import com.collab.project.util.AuthUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(value = "/api/v1")
public class EmailController {

    @Autowired
    EmailService emailService;

    @Autowired
    EmailHistoryService emailHistoryService;

    @Autowired
    ArtistRepository artistRepository;

    @Autowired
    ScriptServiceImpl scriptService;

    @PostMapping
    @RequestMapping(value = "/notify/all", method = RequestMethod.POST)
    public ResponseEntity<SuccessResponse> sendEmailToAllUsers(@RequestBody EmailNotifyInput input) {
        List<Artist> artist = artistRepository.findAll();
        emailService.sendEmailToAllUsersFromString(artist, input.getSubject(), input.getContent());
        return new ResponseEntity<>(new SuccessResponse(), HttpStatus.OK);
    }

    @PostMapping
    @RequestMapping(value = "/notify/user", method = RequestMethod.POST)
    public ResponseEntity<SuccessResponse> sendEmailToOneUser(@RequestBody EmailNotifyInput input) throws MessagingException,
            GeneralSecurityException, IOException {
        String artistId = AuthUtils.getArtistId();
        Artist artist = artistRepository.findByArtistId(artistId);
        emailService.sendEmailToArtist(artist, input.getSubject(), input.getContent());
        return new ResponseEntity<>(new SuccessResponse(), HttpStatus.OK);
    }

    @PostMapping
    @RequestMapping(value = "/notify/user/any", method = RequestMethod.POST)
    public ResponseEntity<SuccessResponse> sendEmailToOneUserBySlug(@RequestBody EmailNotifyInputSlug input) throws MessagingException,
            GeneralSecurityException, IOException {
        List<Artist> artists = artistRepository.findBySlug(input.getSlug());
        emailService.sendEmailFromStringToList(artists, input.getSubject(), input.getContent());
        return new ResponseEntity<>(new SuccessResponse(), HttpStatus.OK);
    }

    @PostMapping
    @RequestMapping(value = "/notify/group/{group_enum}", method = RequestMethod.POST)
    public ResponseEntity<SuccessResponse> sendEmailToOneUser(@PathVariable String group_enum, @RequestBody EmailNotifyInput input) throws MessagingException,
            GeneralSecurityException, IOException {
        if (group_enum.equals("ADMINS")) {
            List<Artist> artists = new ArrayList<>();
            artists.add(artistRepository.findByEmail("prashant.joshi056@gmail.com"));
            artists.add(artistRepository.findByEmail("rahulgupta6007@gmail.com"));
            emailService.sendEmailFromStringToList(artists, input.getSubject(), input.getContent());
        } else if (Constants.EmailGroups.contains(group_enum)) {
            scriptService.emailIncompleteProfileUsers(false);
        }
        return new ResponseEntity<>(new SuccessResponse(), HttpStatus.OK);
    }

    @GetMapping
    @RequestMapping(value = "/email/enums/all", method = RequestMethod.GET)
    public ResponseEntity<SuccessResponse> fetchAllEnumDetails() {
        return new ResponseEntity<>(new SuccessResponse(emailHistoryService.getAllEmailEnumsHistory()), HttpStatus.OK);
    }
}
