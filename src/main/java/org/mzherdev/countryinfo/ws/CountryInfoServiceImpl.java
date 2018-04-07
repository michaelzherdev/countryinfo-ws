package org.mzherdev.countryinfo.ws;

import lombok.extern.slf4j.Slf4j;
import org.mzherdev.countryinfo.exception.CountryInfoException;
import org.mzherdev.countryinfo.jdbc.Statements;
import org.mzherdev.countryinfo.sideapi.CbrWsConnector;
import org.mzherdev.countryinfo.sideapi.GismeteoWsConnector;
import org.mzherdev.countryinfo.model.*;

import javax.jws.HandlerChain;
import javax.jws.WebService;
import java.util.Objects;

@Slf4j
@WebService(endpointInterface = "org.mzherdev.countryinfo.ws.CountryInfoService", targetNamespace = "http://mzherdev.org/")
public class CountryInfoServiceImpl implements CountryInfoService {

    @Override
    public CountryInfo getByCountryName(String countryName) {
        Country country = Statements.getCountryByName(countryName);
        if(Objects.isNull(country))
            throw new CountryInfoException("No such country provided by this service: " + countryName + ". Please provide valid county name on russian.");
        ValuteCourse valuteCourse = null;
        Forecast forecast = null;
        try {
            valuteCourse = CbrWsConnector.getCourse(country.getCurrencyCode());
            forecast = GismeteoWsConnector.getForecast(country.getName(), country.getCapital());
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new CountryInfoException(e.getLocalizedMessage());
        }
        return (Objects.isNull(forecast) || Objects.isNull(valuteCourse))
                ? new CountryInfo(CountryInfo.Status.FAIL)
                : new CountryInfo(CountryInfo.Status.SUCCESS, forecast, valuteCourse);
    }
}
