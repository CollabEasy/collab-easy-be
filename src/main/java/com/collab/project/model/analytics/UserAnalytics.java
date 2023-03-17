package com.collab.project.model.analytics;

import java.util.Comparator;
import java.util.List;

class DateWiseUsers {
    String date;
    int count;

    public DateWiseUsers(String date, int count) {
        this.date = date;
        this.count = count;
    }

    public DateWiseUsers() {

    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
public class UserAnalytics {
    int totalUsers;
    List<DateWiseUsers> dateWiseUsersList;

    public UserAnalytics(int totalUsers, List<DateWiseUsers> dateWiseUsersList) {
        this.totalUsers = totalUsers;
        this.dateWiseUsersList = dateWiseUsersList;
    }

    public int getTotalUsers() {
        return totalUsers;
    }

    public void setTotalUsers(int totalUsers) {
        this.totalUsers = totalUsers;
    }

    public List<DateWiseUsers> getDateWiseUsersList() {
        return dateWiseUsersList;
    }

    public void addNewDateUserDetail(String date, int count) {
        if (dateWiseUsersList == null) return;
        dateWiseUsersList.add(new DateWiseUsers(date, count));
    }

    public void sortOnDate() {
        dateWiseUsersList.sort(Comparator.comparing(DateWiseUsers::getDate));
    }

    public void setDateWiseUsersList(List<DateWiseUsers> dateWiseUsersList) {
        this.dateWiseUsersList = dateWiseUsersList;
    }
}
