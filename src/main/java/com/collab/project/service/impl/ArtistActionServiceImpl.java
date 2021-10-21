package com.collab.project.service.impl;

import com.collab.project.model.artist.ArtistAction;
import com.collab.project.repositories.ArtistActionRepository;
import com.collab.project.service.ArtistActionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

@Service
public class ArtistActionServiceImpl implements ArtistActionService {

    @Autowired
    ArtistActionRepository artistActionRepository;

    @Override
    public void saveArtistAction(ArtistAction artistAction) {
        artistActionRepository.save(artistAction);
    }

    @Override
    public ArtistAction getArtistActionDetails(String artistId, String actionType) {
        return artistActionRepository.findByArtistIdAndActionType(artistId, actionType);
    }

    @Override
    public List<ArtistAction> getAllArtistActionDetails(String artistId) {
        return artistActionRepository.findByArtistId(artistId);
    }

    @Override
    public void updateTimestamp(String artistId, String actionType) {
        ArtistAction action = artistActionRepository.findByArtistIdAndActionType(artistId, actionType);
        if (action == null) {
            action = new ArtistAction(artistId, actionType, "", Timestamp.from(Instant.now()));
        }
        action.setUpdatedAt(Timestamp.from(Instant.now()));
        artistActionRepository.save(action);
    }
}
