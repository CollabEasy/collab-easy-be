package com.collab.project.model.rewards;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "total_points")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TotalPoints {
    @Id
    @Column(name = "artist_id")
    private String artistId;

    @Column(name = "total_points")
    private int totalPoints;

    @Column(name = "lifetime_points")
    private int lifetimePoints;
}
