package com.collab.project.model.analytics;

import lombok.Getter;
import lombok.Setter;

import java.util.Comparator;
import java.util.List;


@Getter
@Setter
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

@Getter
@Setter
class CountryWiseData {
    String country;
    int count;

    public CountryWiseData(String date, int count) {
        this.country = date;
        this.count = count;
    }

    public CountryWiseData() {

    }


    public String getCountry() {
        return country;
    }

    public void setCountry(String date) {
        this.country = date;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}

@Getter
@Setter
public class UserAnalytics {
    int totalUsers;
    List<DateWiseUsers> dateWiseUsersList;
    List<CountryWiseData> countryWiseData;

    public UserAnalytics(int totalUsers, List<DateWiseUsers> dateWiseUsersList, List<CountryWiseData> countryWiseData) {
        this.totalUsers = totalUsers;
        this.dateWiseUsersList = dateWiseUsersList;
        this.countryWiseData = countryWiseData;
    }

    public void addNewDateUserDetail(String date, int count) {
        if (dateWiseUsersList == null) return;
        dateWiseUsersList.add(new DateWiseUsers(date, count));
    }

    public void addNewCountryUserDetail(String country, int count) {
        if (countryWiseData == null) return;
        countryWiseData.add(new CountryWiseData(country, count));
    }

    public void sortOnDate() {
        dateWiseUsersList.sort(Comparator.comparing(DateWiseUsers::getDate));
    }

}
