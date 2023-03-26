package com.collab.project.helpers;

import com.collab.project.model.artist.Artist;
import com.collab.project.model.notification.Notification;
import com.collab.project.repositories.ArtistRepository;
import com.collab.project.repositories.NotificationRepository;
import com.collab.project.service.NotificationService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Component
public class NotificationHelper {

    @Autowired
    NotificationService notificationService;

    @Autowired
    ArtistRepository artistRepository;

    public void createCollabRequestNotification(String toArtist, String fromArtist, String collabId) {
        Map<String, String> metadata = new HashMap();
        Artist artist = artistRepository.findByArtistId(fromArtist);
        if (artist == null) return;
        metadata.put("from_artist", fromArtist);
        metadata.put("from_artist_name", artist.getFirstName() + " " + artist.getLastName());
        metadata.put("from_artist_slug", artist.getSlug());
        Gson gson = new Gson();
        String jsonMetadata = gson.toJson(metadata);

        Notification notification = new Notification(
                toArtist,
                Constants.NOTIFICATION_COLLAB_REQUEST_SENT,
                collabId,
                jsonMetadata,
                false,
                Timestamp.from(Instant.now())
        );

        notificationService.addNotification(notification);
    }

    public void createCollabAcceptedNotification(String toArtist, String fromArtist, String collabId) {
        Map<String, String> metadata = new HashMap();
        Artist artist = artistRepository.findByArtistId(fromArtist);
        if (artist == null) return;
        metadata.put("from_artist", fromArtist);
        metadata.put("from_artist_name", artist.getFirstName() + " " + artist.getLastName());
        metadata.put("from_artist_slug", artist.getSlug());
        Gson gson = new Gson();
        String jsonMetadata = gson.toJson(metadata);

        Notification notification = new Notification(
                toArtist,
                Constants.NOTIFICATION_COLLAB_REQUEST_ACCEPTED,
                collabId,
                jsonMetadata,
                false,
                Timestamp.from(Instant.now())
        );

        notificationService.addNotification(notification);
    }

    public void createCollabRejectedNotification(String toArtist, String fromArtist, String collabId) {
        Map<String, String> metadata = new HashMap();
        Artist artist = artistRepository.findByArtistId(fromArtist);
        if (artist == null) return;
        metadata.put("from_artist", fromArtist);
        metadata.put("from_artist_name", artist.getFirstName() + " " + artist.getLastName());
        metadata.put("from_artist_slug", artist.getSlug());
        Gson gson = new Gson();
        String jsonMetadata = gson.toJson(metadata);

        Notification notification = new Notification(
                toArtist,
                Constants.NOTIFICATION_COLLAB_REQUEST_REJECTED,
                collabId,
                jsonMetadata,
                false,
                Timestamp.from(Instant.now())
        );

        notificationService.addNotification(notification);
    }

    public void createCollabCommentReceivedNotification(String toArtist, String fromArtist, String collabId) {
        if (notificationService.hasUnreadCommentNotification(fromArtist, toArtist)) return;
        Map<String, String> metadata = new HashMap();
        Artist artist = artistRepository.findByArtistId(fromArtist);
        if (artist == null) return;
        metadata.put("from_artist", fromArtist);
        metadata.put("from_artist_name", artist.getFirstName() + " " + artist.getLastName());
        metadata.put("from_artist_slug", artist.getSlug());
        Gson gson = new Gson();
        String jsonMetadata = gson.toJson(metadata);

        Notification notification = new Notification(
                toArtist,
                Constants.NOTIFICATION_COLLAB_COMMENT_RECEIVED,
                collabId,
                jsonMetadata,
                false,
                Timestamp.from(Instant.now())
        );

        notificationService.addNotification(notification);
    }
}
