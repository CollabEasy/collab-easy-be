package com.collab.project.repositories;

import com.collab.project.model.artist.ArtistCategory;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArtistCategoryRepository extends JpaRepository<ArtistCategory, Long> {

    public List<ArtistCategory> findByArtistId(String artistId);

    public List<ArtistCategory> findByArtId(Integer artId);

    public ArtistCategory findByArtistIdAndArtId(String artistId,Long artId);
}
