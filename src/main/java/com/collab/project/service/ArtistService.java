package com.collab.project.service;


import com.collab.project.model.inputs.ArtistInput;

public interface ArtistService {

    public void createArtist(ArtistInput artistInput);

    public void updateArtist();
}
