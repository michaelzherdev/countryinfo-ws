package org.mzherdev.countryinfo.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CountryInfo {

    public enum Status { SUCCESS, FAIL }

    Status status;
    String date;
    Forecast forecast;
    ValuteCourse rate;

    public CountryInfo(Status status, Forecast forecast, ValuteCourse course) {
        this.status = status;
        this.forecast = forecast;
        this.rate = course;
        this.date = DateTimeFormatter.ISO_DATE.format(LocalDate.now());
    }

    public CountryInfo(Status status) {
        this.status = status;
        this.date =  DateTimeFormatter.ISO_DATE.format(LocalDate.now());
    }
}
