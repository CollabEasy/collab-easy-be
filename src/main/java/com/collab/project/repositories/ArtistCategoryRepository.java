package com.collab.project.repositories;

import com.collab.project.model.artist.ArtistCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArtistCategoryRepository extends JpaRepository<ArtistCategory, String> {
}
