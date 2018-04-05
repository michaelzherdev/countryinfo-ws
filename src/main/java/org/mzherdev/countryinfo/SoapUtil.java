package org.mzherdev.countryinfo;

import lombok.extern.slf4j.Slf4j;
import org.mzherdev.countryinfo.model.Forecast;
import org.mzherdev.countryinfo.model.ValuteCourse;

import javax.xml.soap.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Objects;

import static org.mzherdev.countryinfo.CbrUtil.CBR_ENDPOINT_URL;
import static org.mzherdev.countryinfo.CbrUtil.CBR_NAMESPACE;
import static org.mzherdev.countryinfo.GismeteoUtil.*;

@Slf4j
public class SoapUtil {

    static final String DEFAULT_COUNTRY_CODE = "RUS";

    public static ValuteCourse sendCbrSoap() throws Exception {
        SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
        SOAPConnection soapConnection = soapConnectionFactory.createConnection();

        SOAPMessage cbrSoapRequest = CbrUtil.createSOAPMessage(CBR_NAMESPACE, "", LocalDate.now());
        SOAPMessage cbrSoapResponse = soapConnection.call(cbrSoapRequest, CBR_ENDPOINT_URL);
//        printRequestAndResult(cbrSoapRequest, cbrSoapResponse);

        soapConnection.close();
        ValuteCourse course = CbrUtil.parseSOAPResponse(cbrSoapResponse, "USD");
        return course;
    }

    public static Forecast sendGismeteoSoap(String country, String city) throws Exception {
        Objects.requireNonNull(country);
        Objects.requireNonNull(city);

        SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
        SOAPConnection soapConnection = soapConnectionFactory.createConnection();

        // if need to register another user execute this request and change GismeteoUtil.API_KEY constant
//        SOAPMessage registerSoapRequest = GismeteoUtil.createRegistrationSOAPMessage(GISMETEO_NAMESPACE, "", USERNAME, EMAIL);
//        SOAPMessage registerSoapResponse = soapConnection.call(registerSoapRequest, REGISTRATION_ENDPOINT_URL);
//        printRequestAndResult(registerSoapRequest, registerSoapResponse);

        SOAPMessage gismeteoSoapRequest = GismeteoUtil.createLocationSOAPMessage(GISMETEO_NAMESPACE, "", city);
        SOAPMessage gismeteoSoapResponse = soapConnection.call(gismeteoSoapRequest, LOCATION_ENDPOINT_URL);
//        printRequestAndResult(gismeteoSoapRequest, gismeteoSoapResponse);
        String locationId = parseLocationSOAPResponse(gismeteoSoapResponse, country, city);

        gismeteoSoapRequest = GismeteoUtil.createWeatherSOAPMessage(GISMETEO_NAMESPACE, "", locationId);
        gismeteoSoapResponse = soapConnection.call(gismeteoSoapRequest, WEATHER_ENDPOINT_URL);
//        printRequestAndResult(gismeteoSoapRequest, gismeteoSoapResponse);

        soapConnection.close();

        Forecast forecast = parseWeatherSOAPResponse(gismeteoSoapResponse);
        return forecast;
    }

    private static void printRequestAndResult(SOAPMessage request, SOAPMessage response) throws IOException, SOAPException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        request.writeTo(baos);
        log.info(baos.toString());
        baos = new ByteArrayOutputStream();
        response.writeTo(baos);
        log.info(baos.toString());
        baos.close();
//        System.out.println("Request SOAP Message:");
//        request.writeTo(System.out);
//        System.out.println("\nResponse SOAP Message:");
//        response.writeTo(System.out);
    }

    public static void main(String[] args) throws Exception {
        ValuteCourse valuteCourse = sendCbrSoap();
        Forecast forecast = sendGismeteoSoap("Россия", "Тула");
    }


}
