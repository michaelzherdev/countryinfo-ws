package org.mzherdev.countryinfo.jdbc;

import lombok.extern.slf4j.Slf4j;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

@Slf4j
public class DBConnector {
    public static Connection getConnection() {
        Connection con = null;
        try {
            Properties properties = new Properties();
            properties.load(new FileInputStream("src/main/resources/db.properties"));
            String url = properties.getProperty("db.url");
            String user = properties.getProperty("db.user");
            String password = properties.getProperty("db.password");
            con = DriverManager.getConnection
                    (String.format("%s?user=%s&password=%s", url, user, password));
//                    ("jdbc:postgresql://localhost:5432/countryinfo?user=user&password=user");
        } catch (Exception e) {
            log.error("Error in connection" + e);
        }
        return con;
    }
}
