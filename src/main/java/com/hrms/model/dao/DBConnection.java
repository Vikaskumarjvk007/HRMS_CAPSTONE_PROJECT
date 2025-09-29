package com.hrms.model.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    // Fallback local DB credentials (used if env variables are not set)
    private static final String LOCAL_URL = "jdbc:mysql://localhost:3306/hrms_project";
    private static final String LOCAL_USER = "root";
    private static final String LOCAL_PASSWORD = "Aravind@123";

    public static Connection getConnection() throws SQLException {
        // Try to get credentials from environment variables first (GitHub Actions)
        String url = System.getenv("DB_URL");
        String user = System.getenv("DB_USER");
        String password = System.getenv("DB_PASSWORD");

        // If env variables are not set, use local credentials
        if (url == null || user == null || password == null) {
            System.out.println("⚠ Using local database credentials (env vars not set).");
            url = LOCAL_URL;
            user = LOCAL_USER;
            password = LOCAL_PASSWORD;
        }

        // Establish and return the connection
        return DriverManager.getConnection(url, user, password);
    }
}
