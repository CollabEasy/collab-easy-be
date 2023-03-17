package com.collab.project.service.impl;

import com.collab.project.model.analytics.UserAnalytics;
import com.collab.project.model.artist.Artist;
import com.collab.project.repositories.ArtistRepository;
import com.collab.project.repositories.CollabRequestRepository;
import com.collab.project.service.AnalyticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class AnalyticsServiceImpl implements AnalyticsService {

    @Autowired
    ArtistRepository artistRepository;

    @Autowired
    CollabRequestRepository collabRequestRepository;

    @Override
    public List<UserAnalytics> getUsersJoinedCount(long startTime, long endTime) {
        List<Artist> artistList = artistRepository.findByDateBetween(startTime, endTime);
        Map<String, Integer> count = new HashMap();
        String pattern = "dd/MM/yyyy";
        DateFormat df = new SimpleDateFormat(pattern);
        for (Artist artist : artistList) {
            Timestamp joinedOn = artist.getCreatedAt();
            Date joinedDate = new Date(String.valueOf(joinedOn));
            String joinedDateStr = df.format(joinedDate);
            count.put(joinedDateStr, count.getOrDefault(joinedDate, 0) + 1);
        }
        List<UserAnalytics> analytics = new ArrayList<>();
        for (Map.Entry entry : count.entrySet()) {
            analytics.add(new UserAnalytics(String.valueOf(entry.getKey()), (Integer)entry.getValue()));
        }
        Collections.sort(analytics, (a1, a2) -> a1.getDate().compareTo(a2.getDate()));
        return analytics;
    }

    @Override
    public Map<String, Integer> getCollabsCreatedCount(long startTime, long endTime) {
        return null;
    }
}
