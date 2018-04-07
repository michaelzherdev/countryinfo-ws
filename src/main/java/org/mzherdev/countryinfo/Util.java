package org.mzherdev.countryinfo;

import lombok.extern.slf4j.Slf4j;
import org.mzherdev.countryinfo.model.Forecast;
import org.mzherdev.countryinfo.model.ValuteCourse;

import javax.xml.soap.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Objects;

import static org.mzherdev.countryinfo.sideapi.GismeteoWsConnector.*;

@Slf4j
public class Util {

    public static void log(SOAPMessage message) {
        try(ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            message.writeTo(baos);
            log.info(baos.toString());
        } catch (IOException | SOAPException e) {
            log.error(e.getMessage());
        }
    }

}
