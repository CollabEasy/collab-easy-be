package com.collab.project.service.impl;

import com.collab.project.model.art.ArtCategory;
import com.collab.project.model.inputs.ArtistInput;
import com.collab.project.model.inputs.ArtistSocialProspectusInput;
import com.collab.project.model.inputs.CategoryInput;
import com.collab.project.model.socialprospectus.ArtistSocialProspectus;
import com.collab.project.repositories.ArtCategoryRepository;
import com.collab.project.service.CategoryService;
import com.collab.project.util.AuthUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.collab.project.helpers.Constants.FALLBACK_ID;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private ArtCategoryRepository artCategoryRepository;

    @Override
    public ArtCategory addCategory(CategoryInput categoryInput) {
        ArtCategory category = artCategoryRepository.findByArtName(categoryInput.getArtName());
        if (category == null) {
            category = new ArtCategory(FALLBACK_ID,
                    categoryInput.getArtName(), categoryInput.getDescription(), categoryInput.getApproved());
        } else {
            category.setDescription(categoryInput.getDescription());
        }
        return artCategoryRepository.save(category);
    }

    @Override
    public boolean delete(CategoryInput categoryInput) {
        ArtCategory category = artCategoryRepository.findByArtName(categoryInput.getArtName());
        if (category != null) {
            artCategoryRepository.delete(category);
        }
        return true;
    }

    @Override
    public ArtCategory getCategoryBySlug(String slug) {
        List<ArtCategory> arts = artCategoryRepository.findBySlug(slug);
        return arts.get(0);
    }

    @Override
    public List<ArtCategory> getDefaultCategory() {

        return artCategoryRepository.findAll();
    }
}
