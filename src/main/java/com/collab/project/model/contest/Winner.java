package com.collab.project.model.contest;

import com.collab.project.model.art.ArtCategory;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.TypeDef;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class Winner {

    String artistId;

    String artistName;

    String artistSlug;

    String profilePicUrl;

    List<ArtCategory> artCategories;
}
