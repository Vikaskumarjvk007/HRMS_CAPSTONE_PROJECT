package com.hrms.model.service;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;

public class PayrollJobs {

    // Fallback local DB credentials
    private static final String LOCAL_URL = "jdbc:mysql://localhost:3306/hrms_project";
    private static final String LOCAL_USER = "root";
    private static final String LOCAL_PASSWORD = "Aravind@123";

    private static final String LOG_FILE = "payroll.log";

    public static void main(String[] args) {
        PayrollJobs payrollJob = new PayrollJobs();
        payrollJob.run();
    }

    public void run() {
        try (PrintWriter logWriter = new PrintWriter(new FileWriter(LOG_FILE, true))) {

            logWriter.println("========== Payroll Job Started: " + LocalDate.now() + " ==========");
            System.out.println("Payroll job started. Check payroll.log for details.");

            // Get DB credentials from environment variables if available
            String dbUrl = System.getenv("DB_URL");
            String dbUser = System.getenv("DB_USER");
            String dbPassword = System.getenv("DB_PASSWORD");

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
                            String msg = "Payroll already generated for " + currentMonth + ". Exiting.";
                            System.out.println(msg);
                            logWriter.println(msg);
                            logWriter.println("========== Payroll Job Ended ==========\n");
                            return;
                        }
                    }
                }

                // Generate payroll if not already done
                String msg = "Payroll not generated for " + currentMonth + ". Generating now...";
                System.out.println(msg);
                logWriter.println(msg);

                PayrollService payrollService = new PayrollService();
                payrollService.generatePayrollForAll();

                msg = "Payroll generation completed for " + currentMonth + ".";
                System.out.println(msg);
                logWriter.println(msg);
                logWriter.println("========== Payroll Job Ended ==========\n");

            } catch (Exception e) {
                String errMsg = "Error during payroll job: " + e.getMessage();
                System.err.println(errMsg);
                logWriter.println(errMsg);
                e.printStackTrace(logWriter);
            }

        } catch (IOException e) {
            System.err.println("Failed to write to payroll.log: " + e.getMessage());
        }
    }
}
