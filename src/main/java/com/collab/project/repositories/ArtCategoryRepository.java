package com.collab.project.repositories;

import com.collab.project.model.art.ArtCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArtCategoryRepository extends JpaRepository<ArtCategory, Long> {
    public ArtCategory findByArtName(String artName);
}
