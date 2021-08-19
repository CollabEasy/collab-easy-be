package com.collab.project.service;


import com.collab.project.controller.ArtistInput;
import com.collab.project.model.artist.Artist;

public interface ArtistService {

    public Artist createArtist(ArtistInput artistInput);

    public void updateArtist();
}
