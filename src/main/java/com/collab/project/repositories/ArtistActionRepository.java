package com.collab.project.repositories;

import com.collab.project.model.artist.ArtistAction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArtistActionRepository extends JpaRepository<ArtistAction, String> {
    public ArtistAction findByArtistIdAndActionType(String artistId, String actionType);

    public List<ArtistAction> findByArtistId(String artistId);
}
