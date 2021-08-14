package com.collab.project.repositories;

import com.collab.project.model.artist.ArtistPreference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArtistPreferenceRepository extends JpaRepository<ArtistPreference, String> {

}
