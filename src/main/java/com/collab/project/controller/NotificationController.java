package com.collab.project.controller;

import com.collab.project.helpers.Constants.ACTIONS;
import com.collab.project.model.artist.ArtistAction;
import com.collab.project.model.collab.CollabRequest;
import com.collab.project.model.inputs.CollabRequestInput;
import com.collab.project.model.inputs.CollabRequestSearch;
import com.collab.project.model.inputs.NotificationReadInput;
import com.collab.project.model.inputs.NotificationSearch;
import com.collab.project.model.notification.Notification;
import com.collab.project.model.notification.NotificationResponse;
import com.collab.project.model.response.SuccessResponse;
import com.collab.project.service.ArtistActionService;
import com.collab.project.service.NotificationService;
import com.collab.project.util.AuthUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

@RestController
// TODO : authenticate request from current user_id.
@Validated
@RequestMapping(value = "/api/v1/notification", headers = "Accept=application/json")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private ArtistActionService artistActionService;

    @PostMapping(value = "/search")
    public ResponseEntity<SuccessResponse> getNotifications(@RequestBody @Validated NotificationSearch notificationSearch) {
        String artistId = "ae7c46fa-3228-11ec-8d3d-0242ac130003";
        List<Notification> notifications = notificationService.getNotifications(artistId, notificationSearch);
        return new ResponseEntity<>(new SuccessResponse(notifications), HttpStatus.OK);
    }

    @PostMapping(value = "/read")
    public ResponseEntity<SuccessResponse> markRead(@RequestBody NotificationReadInput notificationInput) {
        notificationService.markRead(notificationInput.getNotificationIds());
        return new ResponseEntity<>(new SuccessResponse(), HttpStatus.OK);
    }

    @GetMapping(value = "/all")
    public ResponseEntity<SuccessResponse> getAllNotifications() {
//        String artistId = AuthUtils.getArtistId();
        String artistId = "ae7c46fa-3228-11ec-8d3d-0242ac130003";
        List<Notification> notifications = notificationService.getAllNotifications(artistId);
        ArtistAction artistAction = artistActionService.getArtistActionDetails(artistId, ACTIONS.FETCH_NOTIFICATIONS.toString());
        NotificationResponse response = new NotificationResponse(notifications, artistAction);
        artistActionService.updateTimestamp(artistId, ACTIONS.FETCH_NOTIFICATIONS.toString());
        return new ResponseEntity<>(new SuccessResponse(response), HttpStatus.OK);
    }

    @GetMapping(value = "/new")
    public ResponseEntity<SuccessResponse> getNewNotifications() {
        String artistId = "ae7c46fa-3228-11ec-8d3d-0242ac130003";
        ArtistAction artistAction = artistActionService.getArtistActionDetails(artistId, ACTIONS.FETCH_NOTIFICATIONS.toString());
        List<Notification> notifications = notificationService.getNewNotifications(artistId, artistAction == null ? Timestamp.from(Instant.EPOCH) : artistAction.getUpdatedAt());
        return new ResponseEntity<>(new SuccessResponse(notifications), HttpStatus.OK);
    }
}
