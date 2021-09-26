package com.collab.project.service.impl;

import com.collab.project.exception.CollabRequestException;
import com.collab.project.model.collab.CollabRequest;
import com.collab.project.model.enums.Enums;
import com.collab.project.model.inputs.CollabRequestInput;
import com.collab.project.model.inputs.CollabRequestSearch;
import com.collab.project.model.inputs.NotificationSearch;
import com.collab.project.model.notification.Notification;
import com.collab.project.repositories.CollabRequestRepository;
import com.collab.project.repositories.NotificationRepository;
import com.collab.project.search.SpecificationBuilder;
import com.collab.project.service.CollabService;
import com.collab.project.service.NotificationService;
import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Override
    public List<Notification> getNotifications(String artistId, NotificationSearch notificationSearch) {
        SpecificationBuilder<Notification> builder =
                new SpecificationBuilder();
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

}
