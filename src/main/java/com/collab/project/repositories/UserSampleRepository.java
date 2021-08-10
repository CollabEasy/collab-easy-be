package com.collab.project.repositories;

import com.collab.project.model.user.UserSample;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserSampleRepository extends JpaRepository<UserSample, String> {
}
