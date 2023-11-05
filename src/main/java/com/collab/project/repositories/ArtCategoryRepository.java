package com.collab.project.repositories;

import com.collab.project.model.art.ArtCategory;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ArtCategoryRepository extends JpaRepository<ArtCategory, Long> {

    public ArtCategory findByArtName(String artName);

    public ArtCategory findBySlug(String slug);

    public Optional<ArtCategory> findById(Long id);

    @Query(value = "SELECT * FROM art_categories where art_name like ?1%", nativeQuery = true)
    public List<ArtCategory> findBySlugStartsWith(String query);

    public List<ArtCategory> findByIdIn(List<Long> ids);
}
