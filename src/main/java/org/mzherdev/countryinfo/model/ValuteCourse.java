package org.mzherdev.countryinfo.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString
public class ValuteCourse {
    String name;
    int nom;
    String course;
    String characterCode;
}
