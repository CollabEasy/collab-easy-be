package com.collab.project.service;

import com.collab.project.model.artist.ArtistCategory;

import java.util.List;

public interface ArtistCategoryService {

    public List<ArtistCategory> addCategory(String artistId, List<String> artName);

    public List<String> getArtistCategories(String artistId);
}
