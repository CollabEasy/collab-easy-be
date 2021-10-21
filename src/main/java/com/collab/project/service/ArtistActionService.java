package com.collab.project.service;

import com.collab.project.model.artist.Artist;
import com.collab.project.model.artist.ArtistAction;

import java.util.List;

public interface ArtistActionService {

    public void saveArtistAction(ArtistAction artistAction);

    public ArtistAction getArtistActionDetails(String artistId, String actionType);

    public List<ArtistAction> getAllArtistActionDetails(String artistId);

    void updateTimestamp(String artistId, String actionType);
}
