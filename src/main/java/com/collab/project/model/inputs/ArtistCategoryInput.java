package com.collab.project.model.inputs;

import java.util.List;
import java.util.Map;
import lombok.Data;

@Data
public class ArtistCategoryInput {
    List<String> artNames;
    Map<String,Boolean> artNamesMap;
}
