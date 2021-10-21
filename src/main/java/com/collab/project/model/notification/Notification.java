package com.collab.project.model.notification;

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
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Map;

@Entity
@Table(name = "notifications")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Notification {

    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private String id;

    @Column(name = "artist_id")
    private String artistId;

    @Column(name = "notif_type")
    private String notifType;

    @Column(name = "redirect_url")
    private String redirectUrl;

    @Column(name = "notification_data")
    private String notificationData;

    @Column(name = "notif_read")
    private Boolean notifRead;

    @Column(name = "notif_view_type")
    private String notifViewType;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "updated_at")
    private Timestamp updatedAt;
}