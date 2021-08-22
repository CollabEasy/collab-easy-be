package com.collab.project.repositories;

import com.collab.project.model.collab.CollabRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

@Repository
public interface CollabRequestRepository extends JpaRepository<CollabRequest, String> {

//  @Query("SELECT c from CollabRequest c WHERE c.sender_id=:senderId and receiver_id=:receiverId and status in :status")
//  public List<CollabRequest> findBySenderAndReceiver(@Param("senderId") String senderId, @Param("receiverId")  String receiverId, @Param("status") List<String> status);
//
//  @Query("SELECT c from CollabRequest c WHERE c.sender_id=:senderId and receiver_id=:receiverId and status in :status")
//  public List<CollabRequest> findByReceiverAndSender(@Param("receiverId")  String receiverId, @Param("senderId") String senderId, @Param("status") List<String> status);

  public List<CollabRequest> findBySenderIdAndRecevierIdAndStatusIn(String senderId, String receiverId, List<String> status);
}
