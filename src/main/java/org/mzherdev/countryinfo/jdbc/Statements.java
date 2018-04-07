package org.mzherdev.countryinfo.jdbc;

import lombok.extern.slf4j.Slf4j;
import org.mzherdev.countryinfo.model.Country;

import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Slf4j
public class Statements {

    public static Country getCountryByName(String name) {
        String sql = "SELECT * FROM countries WHERE name = ?";
        Country country = null;
        try {
            PreparedStatement statement = DBConnector.getConnection().prepareStatement(sql);
            statement.setString(1, name);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                country = new Country();
                country.setId(resultSet.getInt(1));
                country.setName(resultSet.getString(2));
                country.setCapital(resultSet.getString(3));
                country.setCurrencyName(resultSet.getString(4));
                country.setCurrencyCode(resultSet.getString(5));
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        return country;
    }

    public static long insertProviderRequest(SOAPMessage request) {
        String sql = "INSERT INTO req_provider_messages (request_message) VALUES (?)";
        return insertRequest(request, sql);
    }

    public static long insertCountryInfoRequest(SOAPMessage request) {
        String sql = "INSERT INTO req_messages (request_message) VALUES (?)";
        return insertRequest(request, sql);
    }

    public static void insertProviderResponse(SOAPMessage response, long requestId) {
        String sql = "INSERT INTO resp_provider_messages (response_message, req_prov_message_id) VALUES (?,?)";
        insertResponse(response, requestId, sql);
    }

    public static void insertCountryInfoResponse(SOAPMessage response, long requestId) {
        String sql = "INSERT INTO resp_messages (response_message, req_message_id) VALUES (?,?)";
        insertResponse(response, requestId, sql);
    }

    public static void insertProviderRequestAndResponse(SOAPMessage request, SOAPMessage response) {
        long requestId = insertProviderRequest(request);
        insertProviderResponse(response, requestId);
    }

    private static long insertRequest(SOAPMessage request, String sql) {
        try {
            String generatedColumns[] = { "id" };
            PreparedStatement statement = DBConnector.getConnection().prepareStatement(sql, generatedColumns);
            statement.setString(1, convertMessageToString(request));
            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getLong(1);
                }
                else {
                    throw new SQLException("Creating request failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        return -1;
    }

    private static void insertResponse(SOAPMessage response, long requestId, String sql) {
        try {
            PreparedStatement statement = DBConnector.getConnection().prepareStatement(sql);
            statement.setString(1, convertMessageToString(response));
            statement.setLong(2, requestId);
            statement.executeUpdate();
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
    }


    private static String convertMessageToString(SOAPMessage message) {
        String msg = "";
        try(ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            message.writeTo(baos);
            msg = baos.toString();
            log.info(msg);
        } catch (IOException | SOAPException e) {
            log.error(e.getMessage());
        }
        return msg;
    }
}
