package com.collab.project.repositories;

import com.collab.project.model.artist.ArtistCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArtistCategoryRepository extends JpaRepository<ArtistCategory, Long> {

    public List<ArtistCategory> findByArtistId(String artistId);

    public List<ArtistCategory> findByArtId(Integer artId);
}
