package com.collab.project.model.rewards;

import com.collab.project.model.enums.Enums;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

@Entity
@Table(name = "rewards_activity")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RewardsActivity {

    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "created_at ")
    private Timestamp createdAt;

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
