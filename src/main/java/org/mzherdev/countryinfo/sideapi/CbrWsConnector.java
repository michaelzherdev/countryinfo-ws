package org.mzherdev.countryinfo.sideapi;

import org.mzherdev.countryinfo.jdbc.Statements;
import org.mzherdev.countryinfo.model.ValuteCourse;
import org.mzherdev.countryinfo.parser.StaxProcessor;

import javax.xml.soap.*;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;
import java.io.IOException;
import java.time.LocalDate;

public class CbrWsConnector {

    public static final String CBR_NAMESPACE = "http://web.cbr.ru/";
    public static final String CBR_ENDPOINT_URL = "http://www.cbr.ru/DailyInfoWebServ/DailyInfo.asmx";

    private CbrWsConnector() {}

    public static ValuteCourse getCourse(String currencyCode) throws Exception {
        SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
        SOAPConnection soapConnection = soapConnectionFactory.createConnection();

        SOAPMessage cbrSoapRequest = CbrWsConnector.createSOAPMessage(CBR_NAMESPACE, "", LocalDate.now());
        SOAPMessage cbrSoapResponse = soapConnection.call(cbrSoapRequest, CBR_ENDPOINT_URL);

        long requestId = Statements.insertProviderRequest(cbrSoapRequest);
        Statements.insertProviderResponse(cbrSoapResponse, requestId);

        soapConnection.close();
        return parseSOAPResponse(cbrSoapResponse, currencyCode);
    }

    private static SOAPMessage createSOAPMessage(String namespace, String prefix, LocalDate date) throws Exception {
        MessageFactory messageFactory = MessageFactory.newInstance();
        SOAPMessage soapMessage = messageFactory.createMessage();
        SOAPPart soapPart = soapMessage.getSOAPPart();

        SOAPEnvelope envelope = soapPart.getEnvelope();
        envelope.addNamespaceDeclaration(prefix, namespace);

        SOAPBody soapBody = envelope.getBody();
        SOAPElement soapBodyElem = soapBody.addChildElement("GetCursOnDate", prefix);
        SOAPElement soapBodyElem1 = soapBodyElem.addChildElement("On_date", prefix);
        soapBodyElem1.addTextNode(date.toString());
        return soapMessage;
    }

    private static ValuteCourse parseSOAPResponse(SOAPMessage soapMessage, String currencyCode) throws IOException, SOAPException, XMLStreamException {
        StaxProcessor processor = new StaxProcessor(soapMessage);

        ValuteCourse valuteCourse = null;
        while (processor.doUntil(XMLEvent.START_ELEMENT, "ValuteCursOnDate")) {
            valuteCourse = new ValuteCourse();
            valuteCourse.setName(processor.getElementValue("Vname").trim());
            valuteCourse.setNom(Integer.parseInt(processor.getElementValue("Vnom")));
            valuteCourse.setCourse(processor.getElementValue("Vcurs"));
            valuteCourse.setCharacterCode(processor.getElementValue("VchCode"));
            if(currencyCode.equals(valuteCourse.getCharacterCode())) {
                break;
            }
        }
        processor.close();
        return valuteCourse;
    }
}
