package com.collab.project.service.impl;

import com.collab.project.model.analytics.UserAnalytics;
import com.collab.project.model.artist.Artist;
import com.collab.project.repositories.ArtistRepository;
import com.collab.project.repositories.CollabRequestRepository;
import com.collab.project.service.AnalyticsService;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class AnalyticsServiceImpl implements AnalyticsService {

    @Autowired
    ArtistRepository artistRepository;

    @Autowired
    CollabRequestRepository collabRequestRepository;

    @Override
    public UserAnalytics getUsersJoinedCount(String startDate, String endDate) {
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);
        DateTimeFormatter customFormatter = DateTimeFormatter.ofPattern("dd MMM, yyyy");
        Map<String, Integer> count = new HashMap<String, Integer>();
        while (!start.isAfter(end)) {
            count.put(start.format(customFormatter), 0);
            start = start.plusDays(1);
        }
        end = end.plusDays(1);

        startDate += " 00:00:00";
        endDate += " 23:59:59";
        Date startTs = Date.from(start.atStartOfDay().toInstant(ZoneOffset.UTC));
        Date endTs = Date.from(end.atStartOfDay().toInstant(ZoneOffset.UTC));
//        List<Artist> artistList = artistRepository.findArtistBetweenDates(startTs, endTs);
        List<Artist> artistList = artistRepository.findArtistBetweenDatesString(startDate, endDate);
        int total = artistRepository.getTotalArtists();

        SimpleDateFormat sf = new SimpleDateFormat("dd MMM, yyyy");
        for (Artist artist : artistList) {
            Timestamp joinedOn = artist.getCreatedAt();
            Date date = new Date();
            date.setTime(joinedOn.getTime());
            String formattedDate = sf.format(date);
            count.put(formattedDate, count.getOrDefault(formattedDate, 0) + 1);
        }
        UserAnalytics analytics = new UserAnalytics(total, new ArrayList<>());
        for (Map.Entry<String, Integer> entry : count.entrySet()) {
            analytics.addNewDateUserDetail(entry.getKey(), entry.getValue());
        }
        analytics.sortOnDate();
        return analytics;
    }

    @Override
    public Map<String, Integer> getCollabsCreatedCount(String startTime, String endTime) {
        return null;
    }
}
