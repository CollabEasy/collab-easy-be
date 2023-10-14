package com.collab.project.repositories;

import com.collab.project.model.art.WordToCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface WordToCategoryRepository extends JpaRepository<WordToCategory, Long> {
    @Query(value = "SELECT * FROM word_to_categories where word like ?1%", nativeQuery = true)
    public List<WordToCategory> findByWordStartsWith(String word);
}
