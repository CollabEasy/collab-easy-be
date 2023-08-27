package com.collab.project.model.enums;

public class Enums {
    public enum EntityType {
        ARTIST,
        ART,
    }

    public enum CollabStatus {
        PENDING,
        REJECTED,
        ACTIVE,
        COMPLETED,
        EXPIRED,
    }

    public enum RewardTypes {
        REFERRAL_USER,
        REFERRAL_SHARER,
        PROFILE_COMPLETION,
    }
}
