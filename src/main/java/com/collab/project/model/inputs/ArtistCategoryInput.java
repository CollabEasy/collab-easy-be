package com.collab.project.model.inputs;

import java.util.List;
import lombok.Data;

@Data
public class ArtistCategoryInput {
    List<String> artNames;

    Boolean initial;
}
