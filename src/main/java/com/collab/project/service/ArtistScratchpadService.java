package com.collab.project.service;

import java.util.List;

import com.collab.project.model.inputs.ArtistScratchpadInput;
import com.collab.project.model.scratchpad.Scratchpad;

public interface ArtistScratchpadService {
    public Scratchpad addScratchpad(String artistId, ArtistScratchpadInput artistScratchpadInput);
    public Scratchpad getScratchpadByArtist(String artistId);
}