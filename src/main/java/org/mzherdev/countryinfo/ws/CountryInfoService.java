package org.mzherdev.countryinfo.ws;

import com.sun.xml.ws.developer.SchemaValidation;
import org.mzherdev.countryinfo.model.CountryInfo;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlElement;

@WebService(targetNamespace = "http://mzherdev.org/")
public interface CountryInfoService {

    @WebMethod
    CountryInfo getByCountryName(@WebParam(name = "country") @XmlElement(required=true) String country) throws Exception;
}
