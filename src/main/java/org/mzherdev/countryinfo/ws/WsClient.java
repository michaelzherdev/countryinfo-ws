package org.mzherdev.countryinfo.ws;

import lombok.extern.slf4j.Slf4j;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import java.net.URL;

//todo move to test
@Slf4j
public class WsClient {

    public static void main(String[] args) throws Exception {
        URL url = new URL("http://localhost:8080/countryService?wsdl");
        QName qname = new QName("http://mzherdev.org/", "CountryInfoServiceImplService");
//        QName qname = new QName("http://mzherdev.org/", "CountryInfoServiceImpl");
        Service service = Service.create(url, qname);

        CountryInfoService countryInfoService = service.getPort(CountryInfoService.class);

        log.info(countryInfoService.getByCountryName("США").toString());
    }
}
