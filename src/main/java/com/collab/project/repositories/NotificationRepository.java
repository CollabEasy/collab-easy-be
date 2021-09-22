package com.collab.project.repositories;

import com.collab.project.model.collab.CollabRequest;
import com.collab.project.model.notification.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends PagingAndSortingRepository<Notification, Long>, JpaSpecificationExecutor<Notification> {
}
