package com.collab.project.controller;

import com.collab.project.model.collab.CollabRequest;
import com.collab.project.model.inputs.CollabRequestInput;
import com.collab.project.model.inputs.CollabRequestSearch;
import com.collab.project.model.inputs.NotificationSearch;
import com.collab.project.model.notification.Notification;
import com.collab.project.model.response.SuccessResponse;
import com.collab.project.service.CollabService;
import com.collab.project.service.NotificationService;
import com.collab.project.util.AuthUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Positive;
import java.util.List;

@RestController
// TODO : authenticate request from current user_id.
@Validated
@RequestMapping(value = "/api/v1/notification", headers = "Accept=application/json")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @PostMapping(value = "/search")
    public ResponseEntity<SuccessResponse> getNotifications(@RequestBody @Validated NotificationSearch notificationSearch) {
        List<Notification> notifications = notificationService.getNotifications(AuthUtils.getArtistId(), notificationSearch);
        return new ResponseEntity<>(new SuccessResponse(notifications), HttpStatus.OK);
    }

    @GetMapping(value = "/")
    public ResponseEntity<SuccessResponse> getAllNotifications() {
        String artistId = AuthUtils.getArtistId();
        List<Notification> notifications = notificationService.getAllNotifications(artistId);
        return new ResponseEntity<>(new SuccessResponse(notifications), HttpStatus.OK);
    }

    @PostMapping(value = "/read")
    public ResponseEntity<SuccessResponse> readAllNotifications() {
        String artistId = AuthUtils.getArtistId();
        notificationService.readAllNotifications(artistId);
        return new ResponseEntity<>(new SuccessResponse(), HttpStatus.OK);
    }
}
