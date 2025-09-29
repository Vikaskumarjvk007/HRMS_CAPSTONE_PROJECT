package com.hrms.model.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;

public class PayrollJobs {

    // Fallback local DB credentials (used if env variables are not set)
    private static final String LOCAL_URL = "jdbc:mysql://localhost:3306/hrms_project";
    private static final String LOCAL_USER = "root";
    private static final String LOCAL_PASSWORD = "Aravind@123";

    public static void main(String[] args) {
        PayrollJobs payrollJob = new PayrollJobs();
        payrollJob.run();
    }

    public void run() {
        // Try to get DB credentials from environment variables first
        String dbUrl = System.getenv("DB_URL");
        String dbUser = System.getenv("DB_USER");
        String dbPassword = System.getenv("DB_PASSWORD");

        // Use fallback credentials if environment variables are null
        if (dbUrl == null || dbUser == null || dbPassword == null) {
            dbUrl = LOCAL_URL;
            dbUser = LOCAL_USER;
            dbPassword = LOCAL_PASSWORD;
        }

        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword)) {

            LocalDate today = LocalDate.now();
            String currentMonth = today.getYear() + "-" + String.format("%02d", today.getMonthValue());

            // Check if payroll for current month already exists
            String checkSql = "SELECT COUNT(*) FROM payrolls WHERE DATE_FORMAT(pay_date, '%Y-%m') = ?";
            try (PreparedStatement stmt = conn.prepareStatement(checkSql)) {
                stmt.setString(1, currentMonth);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    int count = rs.getInt(1);
                    if (count > 0) {
                        System.out.println("Payroll already generated for " + currentMonth + ". Exiting.");
                        return; // Exit gracefully
                    }
                }
            }

            // If payroll not generated yet, run payroll
            System.out.println("Payroll not generated for " + currentMonth + ". Generating now...");
            PayrollService payrollService = new PayrollService();
            payrollService.generatePayrollForAll();
            System.out.println("Payroll generation completed for " + currentMonth + ".");

        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1); // Exit with error if something goes wrong
        }
    }
}
