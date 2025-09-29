package com.hrms.model.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    // Default local DB credentials
    private static final String LOCAL_URL = "jdbc:mysql://localhost:3306/hrms_project";
    private static final String LOCAL_USER = "root";
    private static final String LOCAL_PASSWORD = "Aravind@123";

    public static Connection getConnection() throws SQLException {
        // Try GitHub Actions environment variables first
        String url = System.getenv("DB_URL");
        String user = System.getenv("DB_USER");
        String password = System.getenv("DB_PASSWORD");

        // Fallback to local credentials if env variables are not set
        if (url == null || user == null || password == null) {
            System.out.println("⚠ Using local database credentials (env vars not set).");
            url = "jdbc:mysql://localhost:3306/hrms_project";
            user = "root";
            password = "Aravind@123";
        }

        return DriverManager.getConnection(url, user, password);
    }
}
