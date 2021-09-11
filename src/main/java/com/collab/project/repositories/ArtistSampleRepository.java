package com.collab.project.repositories;

import com.collab.project.model.artist.ArtSample;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArtistSampleRepository extends JpaRepository<ArtSample, String> {

    public List<ArtSample> findByArtistId(String artistId);
}
