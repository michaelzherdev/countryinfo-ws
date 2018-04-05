package org.mzherdev.countryinfo.ws;

import org.mzherdev.countryinfo.model.CountryInfo;
import org.mzherdev.countryinfo.model.Forecast;
import org.mzherdev.countryinfo.model.Status;
import org.mzherdev.countryinfo.model.ValuteCourse;

import javax.jws.WebService;

import java.util.Date;

import static org.mzherdev.countryinfo.SoapUtil.sendCbrSoap;
import static org.mzherdev.countryinfo.SoapUtil.sendGismeteoSoap;

@WebService(endpointInterface = "org.mzherdev.countryinfo.ws.CountryInfoService", targetNamespace = "http://mzherdev.org/")
public class CountryInfoServiceImpl implements CountryInfoService {
    @Override
    public CountryInfo getByCountryName(String country) throws Exception {
        //todo search cur and capital by country name
        ValuteCourse valuteCourse = sendCbrSoap();
        Forecast forecast = sendGismeteoSoap("Россия", "Тула");
        return new CountryInfo(Status.SUCCESS, new Date(), forecast, valuteCourse);
    }
}
