package com.collab.project.repositories;

import com.collab.project.model.artist.Artist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.Query;

import java.util.List;


@Repository
public interface ArtistRepository extends JpaRepository<Artist, String> {


    Artist findByArtistId(String artistId);
    Artist findByArtistHandle(String artistHandle);

    List<Artist> findBySlug(String queryStr);

    @Query(value = "SELECT * FROM artists WHERE slug like ?1%", nativeQuery = true)
    List<Artist> findBySlugStartsWith(String queryStr);
}
