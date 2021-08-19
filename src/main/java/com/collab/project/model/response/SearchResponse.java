package com.collab.project.model.response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
public class SearchResponse {
    @NotNull
    String entityType;

    String name;

    String slug;

    @NotNull
    Object id;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SearchResponse that = (SearchResponse) o;
        return entityType.equals(that.entityType) && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(entityType, id);
    }
}
