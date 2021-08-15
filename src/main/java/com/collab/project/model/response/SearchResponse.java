package com.collab.project.model.response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SearchResponse {
    String entityType;

    String name;

    String slug;

    Object id;

}
