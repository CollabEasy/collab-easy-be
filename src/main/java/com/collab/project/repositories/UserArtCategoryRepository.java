package com.collab.project.repositories;

import com.collab.project.model.user.UserArtCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserArtCategoryRepository extends JpaRepository<UserArtCategory, String> {
}
