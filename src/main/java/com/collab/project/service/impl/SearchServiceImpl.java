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

import java.util.ArrayList;
import java.util.List;

@Service
public class SearchServiceImpl implements SearchService {

    @Autowired
    ArtistRepository artistRepository;

    @Autowired
    ArtCategoryRepository artCategoryRepository;

    @Override
    public List<SearchResponse> getSearchResults(String queryStr) {
        List<SearchResponse> searchResponseList = new ArrayList<>();
        String queryStrSlug = Strings.replace(queryStr, " ", "-");

        List<ArtCategory> artCategories = artCategoryRepository.findBySlug(queryStrSlug);
        for (ArtCategory artCategory : artCategories) searchResponseList.add(new SearchResponse(Enums.EntityType.ART.toString(),
                artCategory.getArtName(),
                artCategory.getSlug(),
                artCategory.getId()));

        artCategories = artCategoryRepository.findBySlugStartsWith(queryStrSlug);
        for (ArtCategory artCategory : artCategories) searchResponseList.add(new SearchResponse(Enums.EntityType.ART.toString(),
                artCategory.getArtName(),
                artCategory.getSlug(),
                artCategory.getId()));


        List<Artist> artists = artistRepository.findBySlug(queryStrSlug);
        for (Artist artist : artists) searchResponseList.add(new SearchResponse(Enums.EntityType.ARTIST.toString(),
                artist.getFirstName() + " " + artist.getLastName(),
                artist.getSlug(),
                artist.getArtistId()));


        artists = artistRepository.findBySlugStartsWith(queryStrSlug);
        for (Artist artist : artists) searchResponseList.add(new SearchResponse(Enums.EntityType.ARTIST.toString(),
                artist.getFirstName() + " " + artist.getLastName(),
                artist.getSlug(),
                artist.getArtistId()));

        return searchResponseList;
    }
}
