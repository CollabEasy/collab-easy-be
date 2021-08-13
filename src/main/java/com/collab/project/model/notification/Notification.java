package com.collab.project.model.notification;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.util.Map;

@Entity
@Table(name = "notifications")
@Getter
@Setter
public class Notification {
    @Id
    private String notifId;

    private String artistId;

    private String notifType;

    private int redirectId;

    @Column(name = "notification_data")
    private String notificationData;

    private Boolean notifRead;

    private String notifViewType;

    private Timestamp createdAt;
}
