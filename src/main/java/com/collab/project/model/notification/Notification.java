package com.collab.project.model.notification;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.Map;

@Getter
@Setter
public class Notification {
    private String notifId;
    private String userId;
    private String notifType;
    private int redirectId;
    private Map<String, String> notification_data;
    private String notifViewType;
    private Timestamp createdAt;
}
