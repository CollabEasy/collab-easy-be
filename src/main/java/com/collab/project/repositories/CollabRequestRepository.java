package com.collab.project.repositories;

import com.collab.project.model.collab.CollabRequest;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

@Repository
public interface CollabRequestRepository extends PagingAndSortingRepository<CollabRequest, Long>, JpaSpecificationExecutor<CollabRequest> {
  
  public List<CollabRequest> findBySenderIdAndReceiverIdAndStatusIn(String senderId, String receiverId, List<String> status);


}
