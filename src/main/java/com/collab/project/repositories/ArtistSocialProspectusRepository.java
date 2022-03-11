package com.collab.project.repositories;

import com.collab.project.model.scratchpad.Scratchpad;
import com.collab.project.model.socialprospectus.ArtistSocialProspectus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArtistSocialProspectusRepository extends JpaRepository<ArtistSocialProspectus, Long> {
    public List<ArtistSocialProspectus> findByArtistId(String artistId);
}
