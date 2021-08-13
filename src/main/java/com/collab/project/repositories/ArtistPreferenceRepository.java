package com.collab.project.repositories;

import com.collab.project.model.artist.ArtistPreference;
import com.collab.project.model.artist.ArtistPreferenceId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface ArtistPreferenceRepository extends JpaRepository<ArtistPreference, ArtistPreferenceId> {

//    @Modifying
//    @Transactional
//    @Query(value = "INSERT INTO artist_preferences (artist_id, setting_name, setting_values) VALUES " +
//            "(?1, ?2, ?3) ON DUPLICATE KEY UPDATE setting_values = ?3", nativeQuery = true)
//    public void updateArtistPreferences(String artistId, String settingName, Object settingValue);

    public List<ArtistPreference> findByArtistPreferenceId_ArtistId(String artistId);
}
