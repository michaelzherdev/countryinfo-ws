package org.mzherdev.countryinfo.ws;

import org.mzherdev.countryinfo.handler.SoapLoggingHandler;

import javax.xml.ws.Endpoint;
import javax.xml.ws.handler.Handler;
import java.util.List;

public class ServicePublisher {

    public static void main(String[] args) {
        Endpoint endpoint = Endpoint.create(new CountryInfoServiceImpl());

//        List<Source> metadata =  ImmutableList.of(
//                                    new StreamSource(("conf/countryInfoService.wsdl")),
//                                    new StreamSource(("conf/countryInfoServiceSchema.xsd")));
//        endpoint.setMetadata(metadata);

        List<Handler> handlerChain = endpoint.getBinding().getHandlerChain();
        handlerChain.add(new SoapLoggingHandler());
        endpoint.getBinding().setHandlerChain(handlerChain);
        endpoint.publish("http://localhost:8080/countryService");
    }
}
