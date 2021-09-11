package com.collab.project.service.impl;

import com.collab.project.exception.RecordNotFoundException;
import com.collab.project.model.art.ArtCategory;
import com.collab.project.model.artist.ArtistCategory;
import com.collab.project.repositories.ArtCategoryRepository;
import com.collab.project.repositories.ArtistCategoryRepository;
import com.collab.project.service.ArtistCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.collab.project.helpers.Constants.FALLBACK_ID;

@Service
public class ArtistCategoryImpl implements ArtistCategoryService {

    @Autowired
    private ArtistCategoryRepository artistCategoryRepository;

    @Autowired
    private ArtCategoryRepository artCategoryRepository;

    @Override
    public List<ArtistCategory> addCategory(String artistId, List<String> artNames) {
        List<ArtistCategory> savedResults = new ArrayList<>();
        for (String artName : artNames) {
            ArtCategory artCategory = artCategoryRepository.findByArtName(artName);
            if (artCategory == null) {
                continue;
            }
            ArtistCategory category = new ArtistCategory(FALLBACK_ID, artistId, artCategory.getId());
            savedResults.add(artistCategoryRepository.save(category));
        }
        return savedResults;
    }

    @Override
    public List<String> getArtistCategories(String artistId) {
        List<ArtistCategory> artistCategories = artistCategoryRepository.findByArtistId(artistId);
        List<String> artCategories = new ArrayList<>();
        for (ArtistCategory artistCategory : artistCategories) {
            Optional<ArtCategory> artCategory = artCategoryRepository.findById(artistCategory.getArtId());
            artCategory.ifPresent(category -> artCategories.add(category.getArtName()));
        }
        return artCategories;
    }
}