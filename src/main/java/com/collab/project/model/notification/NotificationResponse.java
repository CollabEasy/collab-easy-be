package com.collab.project.model.notification;

import com.collab.project.model.artist.ArtistAction;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class NotificationResponse implements Serializable {

    List<Notification> notifications;

    Integer newNotifications;

    public NotificationResponse(List<Notification> notifications, ArtistAction artistAction) {
        newNotifications = 0;
        this.notifications = notifications;
        Timestamp lastFetchedTime = Timestamp.from(Instant.EPOCH);
        if (artistAction != null && artistAction.getUpdatedAt() != null) {
            lastFetchedTime = artistAction.getUpdatedAt();
        }

        for (Notification notification : notifications) {
            if (notification.getCreatedAt().after(lastFetchedTime))  newNotifications++;
            else break;
        }
    }
}
