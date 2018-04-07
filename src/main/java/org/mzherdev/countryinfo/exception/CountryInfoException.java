package org.mzherdev.countryinfo.exception;

/**
 * Created by Mikhail on 06.04.2018.
 */
public class CountryInfoException extends RuntimeException {

    public CountryInfoException(String msg) {
        super(msg);
    }

    public CountryInfoException(Exception e) {
        super(e);
    }

}
