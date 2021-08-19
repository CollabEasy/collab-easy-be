package com.collab.project.repositories;

import com.collab.project.model.artist.Artist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ArtistRepository extends JpaRepository<Artist, String> {

    Artist findByArtistId(String artistId);
    Artist findByArtistHandle(String artistHandle);

}
