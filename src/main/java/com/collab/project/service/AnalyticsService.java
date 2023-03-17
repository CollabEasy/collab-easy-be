package com.collab.project.service;

import com.collab.project.model.analytics.UserAnalytics;

import java.util.List;
import java.util.Map;

public interface AnalyticsService {
    public List<UserAnalytics> getUsersJoinedCount(long startTime, long endTime);

    public Map<String, Integer> getCollabsCreatedCount(long startTime, long endTime);
}
