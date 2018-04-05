package org.mzherdev.countryinfo;

import org.mzherdev.countryinfo.model.Forecast;
import org.mzherdev.countryinfo.parser.StaxProcessor;

import javax.xml.soap.*;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;
import java.io.IOException;
import java.time.LocalDateTime;

public class GismeteoUtil {

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

    static SOAPMessage createRegistrationSOAPMessage(String namespace, String prefix, String username, String email) throws Exception {
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

    static SOAPMessage createLocationSOAPMessage(String namespace, String prefix, String cityName) throws Exception {
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

    static SOAPMessage createWeatherSOAPMessage(String namespace, String prefix, String locationId) throws Exception {
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

    static String parseLocationSOAPResponse(SOAPMessage soapMessage, String country, String city) throws IOException, SOAPException, XMLStreamException {
        StaxProcessor processor = new StaxProcessor(soapMessage);

        String locationId = "";
        while (processor.doUntil(XMLEvent.START_ELEMENT, "LocationInfoFull")) {
            String id = processor.getElementValue("id");
            String town = processor.getElementValue("town");
            String xmlCountry = processor.getElementValue("country");
            town = town.split(" ")[0];
            if (country.equalsIgnoreCase(xmlCountry) &&
                    city.equalsIgnoreCase(town)) { // берем первое совпадение
                locationId = id;
                break;
            }
        }
        processor.close();
        return locationId;
    }

    static Forecast parseWeatherSOAPResponse(SOAPMessage soapMessage) throws IOException, SOAPException, XMLStreamException {
        StaxProcessor processor = new StaxProcessor(soapMessage);

        Forecast forecast = new Forecast();
        while (processor.doUntil(XMLEvent.START_ELEMENT, "HHForecast")) {
            String time = processor.getElementValue("time");
            LocalDateTime dateTime = LocalDateTime.parse(time);
//            forecast.setDate(dateTime);
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
