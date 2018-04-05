package org.mzherdev.countryinfo.parser;

import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.XMLEvent;
import java.io.*;

public class StaxProcessor implements AutoCloseable {
    private static final XMLInputFactory FACTORY = XMLInputFactory.newInstance();

    private final ByteArrayOutputStream outputStream;
    private final StringReader stringReader;
    private final XMLStreamReader xmlReader;

    public StaxProcessor(SOAPMessage soapMessage) throws XMLStreamException, IOException, SOAPException {
        outputStream = new ByteArrayOutputStream();
        soapMessage.writeTo(outputStream);
        stringReader = new StringReader(new String(outputStream.toByteArray()));
        xmlReader = FACTORY.createXMLStreamReader(stringReader);
    }

    public boolean doUntil(int stopEvent, String value) throws XMLStreamException {
        return doUntilAny(stopEvent, value) != null;
    }

    public String doUntilAny(int stopEvent, String... values) throws XMLStreamException {
        while (xmlReader.hasNext()) {
            int event = xmlReader.next();
            if (event == stopEvent) {
                String xmlValue = getValue(event);
                for (String value : values) {
                    if (value.equals(xmlValue)) {
                        return xmlValue;
                    }
                }
            }
        }
        return null;
    }

    public String getValue(int event) throws XMLStreamException {
        return (event == XMLEvent.CHARACTERS) ? xmlReader.getText() : xmlReader.getLocalName();
    }

    public String getElementValue(String element) throws XMLStreamException {
        return doUntil(XMLEvent.START_ELEMENT, element) ? xmlReader.getElementText() : null;
    }

    @Override
    public void close() {
        if(stringReader != null) {
            stringReader.close();
        }
        if(outputStream != null) {
            try {
                outputStream.close();
            } catch (IOException e) {
                // empty
            }
        }
        if (xmlReader != null) {
            try {
                xmlReader.close();
            } catch (XMLStreamException e) {
                // empty
            }
        }
    }
}
