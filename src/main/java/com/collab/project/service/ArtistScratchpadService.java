package com.collab.project.service;

import com.collab.project.model.scratchpad.Scratchpad;

public interface ArtistScratchpadService {
    public Scratchpad addScratchpad(String artistId, String content);
    public Scratchpad getScratchpadByArtistId(String artistId);
}