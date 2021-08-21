package com.collab.project.service;

import com.collab.project.model.response.SearchResponse;

import java.util.List;

public interface SearchService {

    public List<SearchResponse> getSearchResults(String queryStr);
}
