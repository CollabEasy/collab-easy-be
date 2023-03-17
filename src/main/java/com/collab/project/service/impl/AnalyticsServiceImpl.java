package com.collab.project.service.impl;

import com.collab.project.model.analytics.UserAnalytics;
import com.collab.project.model.artist.Artist;
import com.collab.project.repositories.ArtistRepository;
import com.collab.project.repositories.CollabRequestRepository;
import com.collab.project.service.AnalyticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class AnalyticsServiceImpl implements AnalyticsService {

    @Autowired
    ArtistRepository artistRepository;

    @Autowired
    CollabRequestRepository collabRequestRepository;

    @Override
    public List<UserAnalytics> getUsersJoinedCount(String startDate, String endDate) {
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);
        DateTimeFormatter customFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy");
        Map<String, Integer> count = new HashMap<String, Integer>();
        while (!start.isAfter(end)) {
            count.put(start.format(customFormatter), 0);
            start = start.plusDays(1);
        }
        end = end.plusDays(1);

        Timestamp startTs = Timestamp.valueOf(start.atStartOfDay());
        Timestamp endTs = Timestamp.valueOf(end.atStartOfDay());
        List<Artist> artistList = artistRepository.findByCreatedAtBetween(startTs, endTs);
        System.out.println("Fetched artists : " + artistList.size());

        SimpleDateFormat sf = new SimpleDateFormat("MMM dd, yyyy");
        for (Artist artist : artistList) {
            Timestamp joinedOn = artist.getCreatedAt();
            Date date = new Date();
            date.setTime(joinedOn.getTime());
            String formattedDate = sf.format(date);
            count.put(formattedDate, count.getOrDefault(formattedDate, 0) + 1);
        }
        List<UserAnalytics> analytics = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : count.entrySet()) {
            analytics.add(new UserAnalytics(entry.getKey(), entry.getValue()));
        }
        analytics.sort(Comparator.comparing(UserAnalytics::getDate));
        return analytics;
    }

    @Override
    public Map<String, Integer> getCollabsCreatedCount(String startTime, String endTime) {
        return null;
    }
}
