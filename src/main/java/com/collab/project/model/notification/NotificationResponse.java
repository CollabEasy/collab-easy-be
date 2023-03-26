package com.collab.project.model.notification;

import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class NotificationResponse {

    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    private String artistId;

    private String notifType;

    private String redirectId;

    private Map<String, Object> notificationData;

    private Boolean notifRead;

    private Timestamp createdAt;

    public NotificationResponse(Notification notification) {
        this.artistId = notification.getArtistId();
        this.notifType = notification.getNotifType();
        this.redirectId = notification.getRedirectId();
        Gson gson = new Gson();
        Map<String, Object> map = new HashMap<String,Object>();
        map = (Map<String,Object>) gson.fromJson(notification.getNotificationData(), map.getClass());
        this.notifRead = notification.getNotifRead();
        this.createdAt = notification.getCreatedAt();
    }
}