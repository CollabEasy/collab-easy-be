package com.collab.project.service;


import com.collab.project.model.artist.Artist;
import com.collab.project.model.inputs.ArtistInput;

public interface ArtistService {

    public Artist createArtist(ArtistInput artistInput);

    public Boolean updateArtist(ArtistInput artistInput);

    public void delete(ArtistInput artistInput);

    public Artist getArtistById(String artistId);
}
