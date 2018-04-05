package org.mzherdev.countryinfo.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CountryInfo {
    int id;
    Status status;
//    LocalDateTime dateTime;
    Date date;
    Forecast forecast;
    ValuteCourse course;

    public CountryInfo(Status status, Forecast forecast, ValuteCourse course) {
        this.status = status;
        this.forecast = forecast;
        this.course = course;
    }

    public CountryInfo(Status status, Date date, Forecast forecast, ValuteCourse course) {
        this.status = status;
        this.date = date;
        this.forecast = forecast;
        this.course = course;
    }
}
