package com.collab.project.model.response;


import com.collab.project.model.artist.ArtSample;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Getter
public class ArtistSampleResponse implements Serializable {
    String artistId;

    List<ArtInfo> artList;

    public ArtistSampleResponse(String artistId, List<ArtSample> artSamples) {
        this.artistId = artistId;
        if (artSamples == null) return;
        artList = new ArrayList<>();

        artSamples.sort((o1, o2) -> -o1.getCreatedAt().compareTo(o2.getCreatedAt()));

        for (ArtSample sample : artSamples) {
            artList.add(new ArtInfo(
                    sample.getCaption(),
                    sample.getFileType(),
                    sample.getOriginalUrl(),
                    sample.getThumbnailUrl(),
                    sample.getCreatedAt())
            );
        }
    }
}

@Getter
@AllArgsConstructor
class ArtInfo {

    String caption;

    String fileType;

    String originalUrl;

    String thumbnailUrl;

    Timestamp createdAt;
}
