package com.collab.project.repositories;

import com.collab.project.model.scratchpad.Scratchpad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArtistScratchpadRepository extends JpaRepository<Scratchpad, String> {
    public Scratchpad findByArtistId(String artistId);
}