package com.collab.project.repositories;

import com.collab.project.model.socialprospectus.SocialPlatform;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface SocialPlatformRepository extends JpaRepository<SocialPlatform, Long> {
    public SocialPlatform findByPlatformName(String platformName);
    public SocialPlatform findByPlatformId(Long platformId);
}