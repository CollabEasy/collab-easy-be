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
    Artist findByEmail(String email);

    List<Artist> findBySlug(String queryStr);

    @Query(value = "SELECT slug FROM artists WHERE slug like ?1% order by created_at desc limit 1", nativeQuery = true)
    String findLastSlugStartsWith(String queryStr);

    @Query(value = "SELECT * FROM artists WHERE slug like ?1%", nativeQuery = true)
    List<Artist> findBySlugStartsWith(String queryStr);

    Long deleteByArtistId(String artistId);
}
