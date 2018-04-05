package org.mzherdev.countryinfo.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

@Getter @Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString
public class Forecast {
//    LocalDateTime date;
    String dayTemperature;
    String nightTemperature;
    double pressure;
    int humidity;
}
