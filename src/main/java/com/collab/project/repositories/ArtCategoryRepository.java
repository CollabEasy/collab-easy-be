package com.collab.project.repositories;

import com.collab.project.model.art.ArtCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArtCategoryRepository extends JpaRepository<ArtCategory, Long> {
    public ArtCategory findByArtName(String artName);

    public List<ArtCategory> findBySlug(String slug);

    @Query(value = "SELECT * FROM art_categories where art_name like ?1%", nativeQuery = true)
    public List<ArtCategory> findBySlugStartsWith(String query);
}
