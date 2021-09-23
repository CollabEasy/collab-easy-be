package com.collab.project.service;

import com.collab.project.model.artist.ArtistCategory;

import com.collab.project.model.inputs.ArtistCategoryInput;
import java.util.List;

public interface ArtistCategoryService {

    public List<ArtistCategory> addCategory(String artistId, ArtistCategoryInput artistCategoryInput);

    public List<String> getArtistCategories(String artistId);

    public List<String> getDefaultCategories();
}
