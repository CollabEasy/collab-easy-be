package com.collab.project.model.rewards;

import com.collab.project.model.enums.Enums;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

@Entity
@Table(name = "rewards_activity")
@AllArgsConstructor
@Getter
@Setter
public class RewardsActivity {
    @NotNull
    @Column(name = "created_at ")
    private Timestamp createdAt;

    @Id
    @Column(name = "artist_id")
    private String artistId;

    @Column(name = "action")
    private String action;

    @Column(name = "points")
    private int points;

    @Column(name = "added")
    private boolean added;

    @Column(name = "details")
    private String details;
}
