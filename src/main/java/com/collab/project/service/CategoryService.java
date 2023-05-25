package com.collab.project.service;

import com.collab.project.model.art.ArtCategory;
import com.collab.project.model.artist.Artist;
import com.collab.project.model.inputs.ArtistInput;
import com.collab.project.model.inputs.CategoryInput;
import com.collab.project.model.socialprospectus.SocialPlatform;

import java.util.List;

public interface CategoryService {

    ArtCategory addCategory(CategoryInput categoryInput);

    public boolean delete(CategoryInput categoryInput);

    ArtCategory getCategoryBySlug(String slug);

    public List<ArtCategory> getDefaultCategory();
}
