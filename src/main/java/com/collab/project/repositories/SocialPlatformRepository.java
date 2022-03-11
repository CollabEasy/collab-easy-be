package com.collab.project.repositories;

import com.collab.project.model.socialprospectus.SocialPlatform;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SocialPlatformRepository extends JpaRepository<SocialPlatform, Long> {
    public SocialPlatform findByName(String name);
    public Optional<SocialPlatform> findById(Long id);
}