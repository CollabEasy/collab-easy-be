package com.collab.project.model.artist;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Getter
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class ArtistPreferenceId implements Serializable {
    @Column(name = "artist_id")
    public String artistId;

    @Column(name = "setting_name")
    public String settingName;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ArtistPreferenceId that = (ArtistPreferenceId) o;
        return Objects.equals(artistId, that.artistId) && Objects.equals(settingName, that.settingName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(artistId, settingName);
    }
}
