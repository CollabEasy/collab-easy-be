package com.collab.project.model.collab;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Table(name = "collab_conversation_read_status")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CollabConversationReadStatus implements Serializable {

    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "collab_id")
    private String collabId;

    @Column(name = "artist_id")
    private String artistId;

}
