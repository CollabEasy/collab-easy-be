package com.collab.project.model.rewards;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ReferralCodeResponse {

    boolean isValid;

    String referrerArtistFirstName;

    String referrerArtistLastName;

    String referrerArtistSlug;
}
