package com.collab.project.model.inputs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CollabCommentInput  {

    private String artistId;
    private String collabId;
    private String content;
}
