package com.collab.project.service.impl;

import com.collab.project.exception.RecordNotFoundException;
import com.collab.project.model.art.ArtCategory;
import com.collab.project.model.artist.Artist;
import com.collab.project.model.artist.ArtistCategory;
import com.collab.project.model.inputs.ArtistCategoryInput;
import com.collab.project.repositories.ArtCategoryRepository;
import com.collab.project.repositories.ArtistCategoryRepository;
import com.collab.project.repositories.ArtistRepository;
import com.collab.project.service.ArtistCategoryService;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.collab.project.helpers.Constants.FALLBACK_ID;

@Service
@Slf4j
public class ArtistCategoryImpl implements ArtistCategoryService {

    @Autowired
    private ArtistCategoryRepository artistCategoryRepository;

    @Autowired
    private ArtCategoryRepository artCategoryRepository;

    @Value("${basic.art.categories:DANCING,PAINTING,CRAFT,MUSIC,VIDEO EDITING}")
    private List<String> BASIC_ART_CATEGORIES;

    @Autowired
    ArtistRepository artistRepository;

    @Override
    public List<ArtistCategory> addCategory(String artistId,
        ArtistCategoryInput artistCategoryInput) {
        List<ArtistCategory> savedResults = new ArrayList<>();
        for (String artName : artistCategoryInput.getArtNames()) {
            ArtCategory artCategory = artCategoryRepository.findByArtName(artName);
            if (artCategory == null) {
                continue;
            }
            ArtistCategory category = new ArtistCategory(FALLBACK_ID, artistId, artCategory.getId(),
                true);
            savedResults.add(artistCategoryRepository.save(category));
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
        return BASIC_ART_CATEGORIES;
    }

    @Override
    public Map<String, Boolean> updateCategory(String artistId,
        ArtistCategoryInput artistCategoryInput) {
        Map<String, Boolean> outMap = new HashMap<>();
        artistCategoryInput.getArtNamesMap().forEach((key, value) -> {
            ArtCategory artCategory = artCategoryRepository.findByArtName(key);
            ArtistCategory category;
            if (artCategory != null) {
                category = artistCategoryRepository
                    .findByArtistIdAndArtId(artistId, artCategory.getId());
                if (Objects.nonNull(category)) {
                    category.setEnabled(value);
                    log.info("Updating Value for ArtistCategoryId {} to value {}", category.getId(),
                        value);
                } else {
                    category = new ArtistCategory(FALLBACK_ID, artistId,
                        artCategory.getId(), true);
                    log.info("Inserting Value for ArtistId {} with artName {}", artistId, key);
                }
                outMap.put(key, value);
                artistCategoryRepository.save(category);
            }
        });

        return outMap;
    }

    @Override
    public Map<String,Object> fetchArtistByArtName(ArtistCategoryInput artistCategoryInput) {
        Pageable pageableRequest = PageRequest.of(artistCategoryInput.getPage(),
            artistCategoryInput.getSize());
        ArtCategory artCategory = artCategoryRepository
            .findByArtName(artistCategoryInput.getArtCategory());
        if (Objects.isNull(artCategory)) {
            log.error("No Category with name {} exist ", artistCategoryInput.getArtCategory());
            return Collections.EMPTY_MAP;
        }
        List<ArtistCategory> artistByArtId = artistCategoryRepository
            .findByArtId(artCategory.getId(), pageableRequest);
        return artistByArtId.stream().map(k -> artistRepository.findByArtistId(k.getArtistId()))
            .filter(k -> Objects.nonNull(k))
            .collect(Collectors.toMap(Artist::getArtistId, Function.identity()));

    }
}
