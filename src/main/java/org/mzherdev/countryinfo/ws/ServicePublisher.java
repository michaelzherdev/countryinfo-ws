package org.mzherdev.countryinfo.ws;

import org.mzherdev.countryinfo.handler.CountryInfoHandler;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.ws.Endpoint;
import javax.xml.ws.handler.Handler;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class ServicePublisher {

    public static void main(String[] args) throws IOException {
        Endpoint endpoint = Endpoint.create(new CountryInfoServiceImpl());

        List<Source> metadata =  Collections.singletonList(
                new StreamSource(new File("conf/wsdl/service.wsdl")));
        endpoint.setMetadata(metadata);

        List<Handler> handlerChain = endpoint.getBinding().getHandlerChain();
        handlerChain.add(new CountryInfoHandler());
        endpoint.getBinding().setHandlerChain(handlerChain);

        endpoint.publish("http://localhost:8080/countryService");
    }
}
