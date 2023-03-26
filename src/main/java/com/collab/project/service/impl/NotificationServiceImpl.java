package com.collab.project.service.impl;

import com.collab.project.model.inputs.NotificationSearch;
import com.collab.project.model.notification.Notification;
import com.collab.project.model.notification.NotificationResponse;
import com.collab.project.repositories.NotificationRepository;
import com.collab.project.search.SpecificationBuilder;
import com.collab.project.service.NotificationService;
import com.google.common.base.Strings;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Override
    public List<Notification> getNotifications(String artistId, NotificationSearch notificationSearch) {
        SpecificationBuilder<Notification> builder =
                new SpecificationBuilder<>();
        builder.with("redirectId", ":", artistId);
        if(!Strings.isNullOrEmpty(notificationSearch.getNotifType())) {
            builder.with("notifType", ":", notificationSearch.getNotifType());
        }
        if(notificationSearch.getNotifRead() != null) {
            builder.with("notifRead", ":", notificationSearch.getNotifRead());
        }

        return notificationRepository.findAll(builder.build());
    }

    public void addNotification(Notification notification) {
        notificationRepository.save(notification);
    }

    @Override
    public List<NotificationResponse> getAllNotifications(String artistId) {
        List<Notification> notifications = notificationRepository.findByArtistId(artistId);
        List<NotificationResponse> response = new ArrayList<>();
        for (Notification notification : notifications) {
            response.add(new NotificationResponse(notification));
        }
        return response;
    }

    @Override
    public boolean hasUnreadCommentNotification(String fromId, String toId) {
        List<Notification> notifications = notificationRepository.findByArtistIdAndNotifRead(toId, false);
        Gson gson = new Gson();
        for (Notification notification : notifications) {
            String metadata = notification.getNotificationData();
            Map<String, Object> map = new HashMap<String,Object>();
            map = (Map<String,Object>) gson.fromJson(metadata, map.getClass());
            if (map.getOrDefault("from_artist", "").equals(fromId)) return true;
        }
        return false;
    }

    @Override
    public void readAllNotifications(String artistId) {
        List<Notification> notifications = notificationRepository.findByArtistIdAndNotifRead(artistId, false);
        for (Notification notification : notifications) {
            notification.setNotifRead(true);
        }
        notificationRepository.saveAll(notifications);
    }

}
