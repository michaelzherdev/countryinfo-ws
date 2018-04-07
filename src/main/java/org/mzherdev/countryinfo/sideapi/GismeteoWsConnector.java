package org.mzherdev.countryinfo.sideapi;

import org.mzherdev.countryinfo.jdbc.Statements;
import org.mzherdev.countryinfo.model.Forecast;
import org.mzherdev.countryinfo.parser.StaxProcessor;

import javax.xml.soap.*;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Objects;

public class GismeteoWsConnector {

    public static final String GISMETEO_NAMESPACE = "http://ws.gismeteo.ru/";

    private static final String BASE_URL = "http://ws.gismeteo.ru/";
    public static final String REGISTRATION_ENDPOINT_URL = BASE_URL + "Registration/Register.asmx";
    public static final String LOCATION_ENDPOINT_URL = BASE_URL + "Locations/Locations.asmx";
    public static final String WEATHER_ENDPOINT_URL = BASE_URL + "Weather/Weather.asmx";

    // registration params name = "test", email = "test@test.com"
    public static final String USERNAME = "test";
    public static final String EMAIL = "test@test.com";
    public static final String API_KEY = "82214a2e-a297-4605-ac79-8980b97dec65";

    private static final String LANGUAGE = "RU";
    private static final String COUNT = "10";

    private GismeteoWsConnector() {}

    public static Forecast getForecast(String country, String city) throws Exception {
        Objects.requireNonNull(country);
        Objects.requireNonNull(city);

        SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
        SOAPConnection soapConnection = soapConnectionFactory.createConnection();

        // if need to register another user execute this request and change GismeteoWsConnector.API_KEY constant
//        SOAPMessage registerSoapRequest = createRegistrationSOAPMessage(GISMETEO_NAMESPACE, "", USERNAME, EMAIL);
//        SOAPMessage registerSoapResponse = soapConnection.call(registerSoapRequest, REGISTRATION_ENDPOINT_URL);
//        Statements.insertProviderRequestAndResponse(registerSoapRequest, registerSoapResponse);

        SOAPMessage gismeteoSoapRequest = createLocationSOAPMessage(GISMETEO_NAMESPACE, "", city);
        SOAPMessage gismeteoSoapResponse = soapConnection.call(gismeteoSoapRequest, LOCATION_ENDPOINT_URL);
        Statements.insertProviderRequestAndResponse(gismeteoSoapRequest, gismeteoSoapResponse);

        String locationId = parseLocationSOAPResponse(gismeteoSoapResponse, country, city);

        gismeteoSoapRequest = createWeatherSOAPMessage(GISMETEO_NAMESPACE, "", Objects.requireNonNull(locationId));
        gismeteoSoapResponse = soapConnection.call(gismeteoSoapRequest, WEATHER_ENDPOINT_URL);
        Statements.insertProviderRequestAndResponse(gismeteoSoapRequest, gismeteoSoapResponse);

        soapConnection.close();

        return parseWeatherSOAPResponse(gismeteoSoapResponse);
    }

    private static SOAPMessage createRegistrationSOAPMessage(String namespace, String prefix, String username, String email) throws Exception {
        MessageFactory messageFactory = MessageFactory.newInstance();
        SOAPMessage soapMessage = messageFactory.createMessage();
        SOAPPart soapPart = soapMessage.getSOAPPart();

        SOAPEnvelope envelope = soapPart.getEnvelope();
        envelope.addNamespaceDeclaration(prefix, namespace);

        SOAPBody soapBody = envelope.getBody();
        SOAPElement soapBodyElem = soapBody.addChildElement("RegisterHHUser", prefix);

        SOAPElement nameElement = soapBodyElem.addChildElement("name", prefix);
        nameElement.addTextNode(username);

        SOAPElement emailElement = soapBodyElem.addChildElement("email", prefix);
        emailElement.addTextNode(email);
        return soapMessage;
    }

    public static SOAPMessage createLocationSOAPMessage(String namespace, String prefix, String cityName) throws Exception {
        MessageFactory messageFactory = MessageFactory.newInstance();
        SOAPMessage soapMessage = messageFactory.createMessage();
        SOAPPart soapPart = soapMessage.getSOAPPart();

        SOAPEnvelope envelope = soapPart.getEnvelope();
        envelope.addNamespaceDeclaration(prefix, namespace);

        SOAPBody soapBody = envelope.getBody();
        SOAPElement soapBodyElem = soapBody.addChildElement("FindByNameFull", prefix);

        SOAPElement serialElement = soapBodyElem.addChildElement("serial", prefix);
        serialElement.addTextNode(API_KEY);
        SOAPElement nameElement = soapBodyElem.addChildElement("name", prefix);
        nameElement.addTextNode(cityName);
        SOAPElement countElement = soapBodyElem.addChildElement("count", prefix);
        countElement.addTextNode(COUNT);
        SOAPElement languageElement = soapBodyElem.addChildElement("language", prefix);
        languageElement.addTextNode(LANGUAGE);
        return soapMessage;
    }

    private static SOAPMessage createWeatherSOAPMessage(String namespace, String prefix, String locationId) throws Exception {
        MessageFactory messageFactory = MessageFactory.newInstance();
        SOAPMessage soapMessage = messageFactory.createMessage();
        SOAPPart soapPart = soapMessage.getSOAPPart();

        SOAPEnvelope envelope = soapPart.getEnvelope();
        envelope.addNamespaceDeclaration(prefix, namespace);

        SOAPBody soapBody = envelope.getBody();
        SOAPElement soapBodyElem = soapBody.addChildElement("GetHHForecast", prefix);

        SOAPElement serialElement = soapBodyElem.addChildElement("serial", prefix);
        serialElement.addTextNode(API_KEY);
        SOAPElement nameElement = soapBodyElem.addChildElement("location", prefix);
        nameElement.addTextNode(locationId);
        return soapMessage;
    }

    private static String parseLocationSOAPResponse(SOAPMessage soapMessage, String country, String city) throws IOException, SOAPException, XMLStreamException {
        StaxProcessor processor = new StaxProcessor(soapMessage);

        String locationId = "";
        while (processor.doUntil(XMLEvent.START_ELEMENT, "LocationInfoFull")) {
            String id = processor.getElementValue("id");
            String town = processor.getElementValue("town");
            String xmlCountry = processor.getElementValue("country");
            town = town.split(" ")[0];
            if (country.equalsIgnoreCase(xmlCountry) &&
                    city.equalsIgnoreCase(town)) {
                locationId = id;
                break;
            }
        }
        processor.close();
        return locationId;
    }

    private static Forecast parseWeatherSOAPResponse(SOAPMessage soapMessage) throws IOException, SOAPException, XMLStreamException {
        StaxProcessor processor = new StaxProcessor(soapMessage);

        Forecast forecast = null;
        while (processor.doUntil(XMLEvent.START_ELEMENT, "HHForecast")) {
            forecast = new Forecast();
            forecast.setDayTemperature(processor.getElementValue("tod"));
            forecast.setNightTemperature(processor.getElementValue("t"));
            forecast.setPressure(Double.parseDouble(processor.getElementValue("p")));
            forecast.setHumidity(Integer.parseInt(processor.getElementValue("humidity")));
            break;
        }
        processor.close();
        return forecast;
    }
}
