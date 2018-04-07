package org.mzherdev.countryinfo.handler;

import lombok.extern.slf4j.Slf4j;
import org.mzherdev.countryinfo.jdbc.Statements;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import java.io.IOException;
import java.util.*;

@Slf4j
public class CountryInfoHandler implements SOAPHandler<SOAPMessageContext> {

    private static final String COUNTRY_INFO_ID = "country_info_id";

    public Set<QName> getHeaders() {
        return null;
    }

    public boolean handleMessage(SOAPMessageContext smc) {
        insert(smc, true);
        return true;
    }

    public boolean handleFault(SOAPMessageContext smc) {
        insert(smc, false);
        return true;
    }

    public void close(MessageContext messageContext) {
    }

    /*
     * Check the MESSAGE_OUTBOUND_PROPERTY in the context
     * to see if this is an outgoing or incoming message.
     */
    private void insert(SOAPMessageContext smc, boolean formatMsg) {
        Boolean outboundProperty = (Boolean)
                smc.get (MessageContext.MESSAGE_OUTBOUND_PROPERTY);

        SOAPMessage message = smc.getMessage();
        if (outboundProperty) {
            Statements.insertCountryInfoResponse(formatMsg ? formatResponse(message) : message, (Long) smc.get(COUNTRY_INFO_ID));
        } else {
            long requestId = Statements.insertCountryInfoRequest(message);
            smc.put(COUNTRY_INFO_ID, requestId);
        }
    }

    private SOAPMessage formatResponse(SOAPMessage message) {
            try {
                Iterator iterator = message.getSOAPBody().getChildElements();
                while (iterator.hasNext()) {
                    Object object = iterator.next();
                    if (object instanceof SOAPElement) {
                        SOAPElement element = (SOAPElement) object;
                        Iterator countryInfos = ((SOAPElement) element.getChildElements().next()).getChildElements();
                        List<SOAPElement> elements = new ArrayList<>();
                        List<SOAPElement> countryInfoElements = new ArrayList<>();
                        while (countryInfos.hasNext()) {
                            Object countryInfoElement = countryInfos.next();
                            if (countryInfoElement instanceof SOAPElement) {
                                SOAPElement e = (SOAPElement) countryInfoElement;
                                if (e.getElementName().getLocalName().equals("status")
                                        || e.getElementName().getLocalName().equals("date"))
                                    elements.add(e);
                                else
                                    countryInfoElements.add(e);
                            }
                        }
                        element.removeContents();
                        for (SOAPElement e : elements)
                            element.addChildElement(e);
                        SOAPElement countryInfo = element.addChildElement(new QName("countryInfo"));
                        for (SOAPElement e : countryInfoElements)
                            countryInfo.addChildElement(e);
                    }
                }
                message.writeTo(System.out);
            } catch (SOAPException | IOException e) {
                log.error(e.getMessage());
            }

            return message;
    }
}
