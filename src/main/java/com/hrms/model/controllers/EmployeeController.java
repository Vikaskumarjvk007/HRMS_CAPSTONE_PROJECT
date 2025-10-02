package com.hrms.model.controllers;

import com.hrms.model.*;
import com.hrms.model.dao.*;
import java.time.LocalDate;
import com.hrms.model.helper.*;



import java.util.*;

public class  EmployeeController {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // ✅ DAO objects
        EmployeeDAO empDAO = new EmployeeDAO();
        AttendanceDAO attDAO = new AttendanceDAO();
        PayrollDAO payDAO = new PayrollDAO();
        DynamoDBService dynamoService = new DynamoDBService();

        System.out.println("=== EMPLOYEE LOGIN ===");

        Employee emp = null;
        int attempts = 0;

        while (attempts < 3 && emp == null) {
            System.out.print("Enter Your Employee ID: ");
            int empId = sc.nextInt();
            sc.nextLine();

//            System.out.print("Enter Your Password: ");
            String password = readNonEmpty(sc, "Enter your password: ");

            emp = empDAO.validateLogin(empId, password);

            if (emp == null) {
                attempts++;
                if (attempts < 3) {
                    System.out.println("❌ Invalid credentials! Attempts left: " + (3 - attempts));
                }
            }
        }

        if (emp == null) {
            System.out.println("🚫 Too many failed attempts! Try again later.");
            return;
        }

        System.out.println("✅ Welcome, " + emp.getName() + "!");

        while (true) {
            System.out.println("\n=== EMPLOYEE MENU ===");
            System.out.println("1. Mark Attendance");
            System.out.println("2. View My Attendance");
            System.out.println("3. Apply for Leave");
            System.out.println("4. View My Leave Requests");
            System.out.println("5. View My Payslips");
            System.out.println("0. Exit");
            System.out.print("Enter choice: ");
            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1 -> {
                    System.out.print("Enter Status (Present/Absent): ");
                    String status = sc.nextLine().trim();
                    Attendance a = new Attendance(0, emp.getId(), LocalDate.now(), status);
                    attDAO.addAttendance(a);
                    System.out.println("✅ Attendance marked for today.");
                }
                case 2 -> {
                    List<Attendance> records = attDAO.getAttendanceByEmployee(emp.getId());
                    if (records.isEmpty()) System.out.println("❌ No attendance found.");
                    else records.forEach(System.out::println);
                }
                case 3 -> {
                    System.out.println("\n--- Apply for Leave ---");

                    // ✅ Pick Start Date
                    System.out.println("📅 Select Start Date:");
                    LocalDate startDate = LeaveCalendar.showCalendarAndPickDate(sc);

                    // ✅ Pick End Date
                    System.out.println("\n📅 Select End Date:");
                    LocalDate endDate = LeaveCalendar.showCalendarAndPickDate(sc);

                    // ✅ Validate dates
                    if (endDate.isBefore(startDate)) {
                        System.out.println("❌ End Date cannot be before Start Date. Please try again.");
                        break;
                    }

                    System.out.print("Enter Reason: ");
                    sc.nextLine(); // consume leftover newline
                    String reason = sc.nextLine();

                    // ✅ Create leave request (status defaults to Pending)
                    LeaveRequest lr = new LeaveRequest(emp.getId(), startDate, endDate, reason);
                    lr.setEmployeeName(emp.getName());

                    dynamoService.addLeaveRequest(lr);

                    System.out.println("✅ Leave request submitted successfully.");
                    System.out.println("👉 Your Request ID is: " + lr.getRequestId());
                }

                case 4 -> {
                    List<LeaveRequest> leaves = dynamoService.getLeaveRequestsByEmployee(emp.getId());
                    if (leaves.isEmpty()) System.out.println("❌ No leave requests found.");
                    else leaves.forEach(System.out::println);
                }
                case 5 -> {
                    List<Payroll> slips = payDAO.getPayrollByEmployee(emp.getId());
                    if (slips.isEmpty()) {
                        System.out.println("❌ No payslips found.");
                    } else {
                        System.out.println("\n=========== My Payslips ===========");
                        System.out.printf("%-12s %-10s %-10s %-12s %-12s %-12s%n",
                                "Date", "Basic", "HRA", "Allowances", "Deductions", "Net Salary");
                        System.out.println("------------------------------------------------------------");
                        for (Payroll p : slips) {
                            System.out.printf("%-12s %-10.2f %-10.2f %-12.2f %-12.2f %-12.2f%n",
                                    p.getPayDate(), p.getBasic(), p.getHra(),
                                    p.getAllowances(), p.getDeductions(), p.getNetSalary());
                        }
                        System.out.println("===================================\n");
                    }
                }
                case 0 -> {
                    System.out.println("👋 Exiting...");
                    return;
                }
                default -> System.out.println("❌ Invalid choice!");
            }
        }
    }

    private static String readNonEmpty(Scanner sc, String prompt) {
        String input;
        do {
            System.out.print(prompt);
            input = sc.nextLine().trim();
            if (input.isEmpty()) {
                System.out.println("❌ This field cannot be empty. Please try again.");
            }
        } while (input.isEmpty());
        return input;
    }

}
