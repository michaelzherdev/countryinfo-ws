package org.mzherdev.countryinfo.handler;

import lombok.extern.slf4j.Slf4j;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Set;

//https://stackoverflow.com/questions/1945618/tracing-xml-request-responses-with-jax-ws
@Slf4j
public class SoapLoggingHandler implements SOAPHandler<SOAPMessageContext> {

    public Set<QName> getHeaders() {
        return null;
    }

    public boolean handleMessage(SOAPMessageContext smc) {
        log(smc);
        return true;
    }

    public boolean handleFault(SOAPMessageContext smc) {
        log(smc);
        return true;
    }

    public void close(MessageContext messageContext) {
    }

    /*
     * Check the MESSAGE_OUTBOUND_PROPERTY in the context
     * to see if this is an outgoing or incoming message.
     */
    private void log(SOAPMessageContext smc) {
        Boolean outboundProperty = (Boolean)
                smc.get (MessageContext.MESSAGE_OUTBOUND_PROPERTY);

        SOAPMessage message = smc.getMessage();
        if (outboundProperty.booleanValue()) {
            log.info("\nOutbound message: " + getMessageText(message));
        } else {
            log.info("\nInbound message: " + getMessageText(message));
        }
    }

    protected static String getMessageText(SOAPMessage msg) {
        try(ByteArrayOutputStream out = new ByteArrayOutputStream()) {            ;
            msg.writeTo(out);
            return out.toString(StandardCharsets.UTF_8.name());
        } catch (Exception e) {
            log.warn("Coudn't get SOAP message for logging", e);
            return null;
        }
    }
}
