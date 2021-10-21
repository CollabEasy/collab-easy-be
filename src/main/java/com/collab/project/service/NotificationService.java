package com.collab.project.service;


import com.collab.project.model.collab.CollabRequest;
import com.collab.project.model.inputs.CollabRequestInput;
import com.collab.project.model.inputs.CollabRequestSearch;
import com.collab.project.model.inputs.NotificationSearch;
import com.collab.project.model.notification.Notification;

import java.sql.Timestamp;
import java.util.List;

public interface NotificationService {

    public List<Notification> getNotifications(String artistId, NotificationSearch notificationSearch);

    public List<Notification> getAllNotifications(String artistId);

    public void addNotification(Notification notification);

    List<Notification> getNewNotifications(String artistId, Timestamp lastFetchedTs);

    void markRead(List<String> notificationIds);
}
