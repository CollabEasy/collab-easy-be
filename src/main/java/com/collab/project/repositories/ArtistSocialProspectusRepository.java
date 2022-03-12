package com.collab.project.repositories;

import com.collab.project.model.scratchpad.Scratchpad;
import com.collab.project.model.socialprospectus.ArtistSocialProspectus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArtistSocialProspectusRepository extends JpaRepository<ArtistSocialProspectus, Long> {
    public List<ArtistSocialProspectus> findByArtistId(String artistId);

    @Query(value = "SELECT * FROM artist_social_prospectus where artist_id = (?1) AND social_platform_id = (?2) ", nativeQuery = true)
    public ArtistSocialProspectus findByArtistAndPlatformId(String artistId, Long platformId);
}
