package com.collab.project.model.artist;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

@Entity
@Table(name = "artist_actions")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ArtistAction {
    @Id
    @NotNull
    @Column(name = "artist_id")
    String artistId;

    @NotNull
    @Column(name = "action_type")
    String actionType;

    @NotNull
    @Column(name = "data")
    String data;

    @NotNull
    @Column(name = "updated_at")
    Timestamp updatedAt;
}
