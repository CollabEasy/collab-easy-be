package com.collab.project.service.impl;

import com.collab.project.model.art.ArtCategory;
import com.collab.project.model.artist.Artist;
import com.collab.project.model.artist.ArtistCategory;
import com.collab.project.model.inputs.ArtistScratchpadInput;
import com.collab.project.model.scratchpad.Scratchpad;
import com.collab.project.repositories.ArtistRepository;
import com.collab.project.repositories.ArtistScratchpadRepository;
import com.collab.project.service.ArtistScratchpadService;
import com.collab.project.util.AuthUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.collab.project.helpers.Constants.FALLBACK_ID;

import java.util.*;

import static com.collab.project.helpers.Constants.FALLBACK_ID;

@Service
public class ArtistScratchpadImpl implements ArtistScratchpadService {

    @Autowired
    private ArtistScratchpadRepository artistScratchpadRepository;


    @Override
    public Scratchpad addScratchpad(String artistId, String content) {
        Scratchpad scratchpad = artistScratchpadRepository.findByArtistId(AuthUtils.getArtistId());
        if (scratchpad == null) {
            scratchpad = new Scratchpad(FALLBACK_ID, AuthUtils.getArtistId(), content);
        } else {
            scratchpad.setContent(content);
        }
        artistScratchpadRepository.save(scratchpad);
        return scratchpad;
    }

    @Override
    public Scratchpad getScratchpadByArtistId(String artistId) {
        Scratchpad scratchpad = artistScratchpadRepository.findByArtistId(artistId);
        if (scratchpad == null) {
            scratchpad = new Scratchpad(FALLBACK_ID, AuthUtils.getArtistId(), "");
            artistScratchpadRepository.save(scratchpad);
        }
        return scratchpad;
    }
}
