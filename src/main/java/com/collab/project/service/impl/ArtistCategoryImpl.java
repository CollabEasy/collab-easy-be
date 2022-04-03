package com.collab.project.service.impl;

import com.collab.project.model.art.ArtCategory;
import com.collab.project.model.artist.*;
import com.collab.project.model.inputs.ArtistCategoryInput;
import com.collab.project.repositories.ArtCategoryRepository;
import com.collab.project.repositories.ArtistCategoryRepository;
import com.collab.project.repositories.ArtistPreferenceRepository;
import com.collab.project.repositories.ArtistRepository;
import com.collab.project.service.ArtistCategoryService;
import com.collab.project.util.AuthUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.collab.project.helpers.Constants.FALLBACK_ID;

@Service
public class ArtistCategoryImpl implements ArtistCategoryService {

    @Autowired
    private ArtistCategoryRepository artistCategoryRepository;

    @Autowired
    private ArtCategoryRepository artCategoryRepository;

    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    ArtistPreferenceRepository artistPreferenceRepository;

    @Value("${basic.art.categories:DANCING,PAINTING,CRAFT,MUSIC,VIDEO EDITING}")
    private List<String> BASIC_ART_CATEGORIES;

    @Override
    public List<ArtistCategory> addCategory(String artistId, ArtistCategoryInput artistCategoryInput) {
        List<ArtistCategory> savedResults = new ArrayList<>();
        List<ArtistCategory> existingArts = artistCategoryRepository.findByArtistId(artistId);
        Set<String> toAdd = new HashSet<String>(artistCategoryInput.getArtNames());

        for (ArtistCategory artistCategory : existingArts) {
            Optional<ArtCategory> artCategory = artCategoryRepository.findById(artistCategory.getArtId());
            if (artCategory.isPresent()) {
                ArtCategory category = artCategory.get();
                if (!toAdd.contains(category.getArtName())) {
                    artistCategoryRepository.delete(artistCategory);
                } else {
                    toAdd.remove(category.getArtName());
                }
            }
        }

        for (String artName : toAdd) {
            ArtCategory artCategory = artCategoryRepository.findByArtName(artName);
            if (artCategory == null) {
                continue;
            }
            ArtistCategory category = new ArtistCategory(FALLBACK_ID, artistId, artCategory.getId());
            savedResults.add(artistCategoryRepository.save(category));
        }
        if (artistCategoryInput.isInitial()) {
            Artist artist = artistRepository.findByArtistId(artistId);
            artist.setNewUser(false);
            artistRepository.save(artist);
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

    @Override
    public List<String> getDefaultCategories() {
        List<ArtCategory> categories = artCategoryRepository.findAll();
        List<String> artNames = new ArrayList<String>();
        for (ArtCategory category : categories) artNames.add(category.getArtName());
        return artNames;
    }

    @Override
    public List<Artist> getArtistsByCategoryId(Long categoryID) {
        List<ArtistCategory> artistCategories = artistCategoryRepository.findByArtId(categoryID);
        List<Artist> artists = new ArrayList<Artist>();
        for (ArtistCategory artistCategory : artistCategories) {
            Artist artist = artistRepository.findByArtistId(artistCategory.getArtistId());
            if (artist != null) artists.add(artist);
        }
        return artists;
    }

    @Override
    public List<SearchedArtistOutput> getArtistsByCategorySlug(String categorySlug) {

        List<SearchedArtistOutput> artists = new ArrayList<SearchedArtistOutput>();
        // Since category slug is unique, there should be only one category associated with the slug.
        List<ArtCategory> artCategory = artCategoryRepository.findBySlug(categorySlug);
        // Since there is one category associated with slug, we can safely fetch first element if it exists.
        if (!artCategory.isEmpty()) {
            List<ArtistCategory> artistCategories = artistCategoryRepository.findByArtId(artCategory.get(0).getId());
            for (ArtistCategory artistCategory : artistCategories) {
                Artist artist = artistRepository.findByArtistId(artistCategory.getArtistId());
                if (artist != null) {
                    Optional<ArtistPreference> preference = artistPreferenceRepository
                            .findById(new ArtistPreferenceId(artist.getArtistId(), "upForCollaboration"));
                    List<String> categories = getArtistCategories(artist.getArtistId());
                    SearchedArtistOutput artistDetails = new SearchedArtistOutput(artist, categories,
                            (preference.isPresent() ? preference.get().getSettingValues(): ""));
                    artists.add(artistDetails);
                }
            }
        }
        return artists;
    }
}
