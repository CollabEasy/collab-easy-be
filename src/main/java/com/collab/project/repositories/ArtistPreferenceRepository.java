package com.collab.project.repositories;

import com.collab.project.model.artist.ArtistPreference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArtistPreferenceRepository extends JpaRepository<ArtistPreference, String> {

    @Query(value = "INSERT INTO artist-preferences (artistId, settingName, settingValue) VALUES " +
            "(?1, ?2, ?3) ON DUPLICATE KEY UPDATE settingValue = ?3", nativeQuery = true)
    public void updateArtistPreferences(@Param("artistId") String artistId,
                                        @Param("settingName") String settingName,
                                        @Param("settingValue") Object settingValue);

    public List<ArtistPreference> findByArtistId(String artistId);

    public ArtistPreference findByArtistIdAndSettingName(String artistId, String settingName);
}
