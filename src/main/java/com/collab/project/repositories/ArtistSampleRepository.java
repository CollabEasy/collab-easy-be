package com.collab.project.repositories;

import com.collab.project.model.artist.ArtSample;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArtistSampleRepository extends JpaRepository<ArtSample, String> {
}
