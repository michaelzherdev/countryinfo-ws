package org.mzherdev.countryinfo.ws;

import org.mzherdev.countryinfo.model.CountryInfo;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

@WebService(targetNamespace = "http://mzherdev.org/")
public interface CountryInfoService {

    @WebMethod
    CountryInfo getByCountryName(@WebParam(name = "country") String country) throws Exception;
}
