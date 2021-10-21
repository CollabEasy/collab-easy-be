package com.collab.project.repositories;

import com.collab.project.model.collab.CollabRequest;
import com.collab.project.model.notification.Notification;
import org.aspectj.weaver.ast.Not;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends PagingAndSortingRepository<Notification, String>, JpaSpecificationExecutor<Notification> {

    public List<Notification> findByArtistId(String artistId);
}
