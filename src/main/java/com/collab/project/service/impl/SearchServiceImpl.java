package com.collab.project.service.impl;

import com.collab.project.model.art.ArtCategory;
import com.collab.project.model.artist.Artist;
import com.collab.project.model.enums.Enums;
import com.collab.project.model.response.SearchResponse;
import com.collab.project.repositories.ArtCategoryRepository;
import com.collab.project.repositories.ArtistRepository;
import com.collab.project.service.SearchService;
import io.jsonwebtoken.lang.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SearchServiceImpl implements SearchService {

    @Autowired
    ArtistRepository artistRepository;

    @Autowired
    ArtCategoryRepository artCategoryRepository;

    private void updateListWithArtCategories(Set<SearchResponse> searchResponseSet,
                            List<SearchResponse> searchResults,
                            List<ArtCategory> artCategories) {
        for (ArtCategory artCategory : artCategories) {
            SearchResponse response = new SearchResponse(Enums.EntityType.ART.toString(),
                    artCategory.getArtName(),
                    artCategory.getSlug(),
                    artCategory.getId());
            if (!searchResponseSet.contains(response)) {
                searchResults.add(response);
                searchResponseSet.add(response);
            }
        }
    }

    private void updateListWithArtists(Set<SearchResponse> searchResponseSet,
                                             List<SearchResponse> searchResults,
                                             List<Artist> artists) {
        for (Artist artist : artists) {
            if (artist.getTestUser().equals(true)) {
                continue;
            }
            SearchResponse response = new SearchResponse(Enums.EntityType.ARTIST.toString(),
                    artist.getFirstName() + " " + artist.getLastName(),
                    artist.getSlug(),
                    artist.getArtistId());
            if (!searchResponseSet.contains(response)) {
                searchResults.add(response);
                searchResponseSet.add(response);
            }
        }
    }

    @Override
    public List<SearchResponse> getSearchResults(String queryStr) {
        Set<SearchResponse> searchResponseSet = new HashSet<>();
        String queryStrSlug = Strings.replace(queryStr, " ", "-");
        List<SearchResponse> searchResults = new ArrayList<>();

        // Storing exact match art names first
        List<ArtCategory> artCategories = artCategoryRepository.findBySlug(queryStrSlug);
        updateListWithArtCategories(searchResponseSet, searchResults, artCategories);

        // Then goes prefix match art names
        artCategories = artCategoryRepository.findBySlugStartsWith(queryStrSlug);
        updateListWithArtCategories(searchResponseSet, searchResults, artCategories);

        // Third priority to the exact match artist names
        List<Artist> artists = artistRepository.findBySlug(queryStrSlug);
        updateListWithArtists(searchResponseSet, searchResults, artists);

        // And the last is prefix matched artist names
        artists = artistRepository.findBySlugStartsWith(queryStrSlug);
        updateListWithArtists(searchResponseSet, searchResults, artists);

        return searchResults;
    }
}
