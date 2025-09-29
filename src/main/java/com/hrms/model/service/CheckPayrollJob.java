package com.hrms.model.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;

public class CheckPayrollJob {

    public static void main(String[] args) {
        // Get DB credentials from environment variables
        String dbUrl = System.getenv("DB_URL");       // e.g., jdbc:mysql://localhost:3306/hrms_db
        String dbUser = System.getenv("DB_USER");
        String dbPassword = System.getenv("DB_PASSWORD");

        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword)) {
            LocalDate today = LocalDate.now();
            String currentMonth = today.getYear() + "-" + String.format("%02d", today.getMonthValue());

            String sql = "SELECT COUNT(*) FROM payroll WHERE DATE_FORMAT(pay_date, '%Y-%m') = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, currentMonth);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    int count = rs.getInt(1);
                    if (count > 0) {
                        System.out.println("Payroll already generated for " + currentMonth + ". Exiting.");
                        System.exit(1); // Exit with 1 → workflow will skip PayrollJob
                    } else {
                        System.out.println("Payroll not generated for " + currentMonth + ". Proceeding...");
                        System.exit(0); // Exit with 0 → workflow will run PayrollJob
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1); // Fail if there’s any error connecting to DB
        }
    }
}
