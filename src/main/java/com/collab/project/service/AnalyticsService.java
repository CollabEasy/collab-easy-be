package com.collab.project.service;

import com.collab.project.model.analytics.UserAnalytics;

import java.util.List;
import java.util.Map;

public interface AnalyticsService {
    public UserAnalytics getUsersJoinedCount(String startTime, String endTime);

    public Map<String, Integer> getCollabsCreatedCount(String startTime, String endTime);

    public Map<String, Integer> getCountryLevelArtists();
}
