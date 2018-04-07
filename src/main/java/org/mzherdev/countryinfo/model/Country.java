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
public class Country {
    int id;
    String name;
    String capital;
    String currencyName;
    String currencyCode;
}
