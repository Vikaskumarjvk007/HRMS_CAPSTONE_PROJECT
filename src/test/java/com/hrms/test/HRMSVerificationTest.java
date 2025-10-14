package com.hrms.test;

import com.hrms.model.*;
import com.hrms.model.dao.*;
import com.hrms.model.service.PayrollService;

import java.time.LocalDate;
import java.util.List;

/**
 * Automated test to verify HRMS application functionality
 */
public class HRMSVerificationTest {

    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("HRMS APPLICATION VERIFICATION TEST");
        System.out.println("========================================\n");

        boolean allTestsPassed = true;

        try {
            // Test 1: Database Connection
            System.out.println("Test 1: Database Connection");
            allTestsPassed &= testDatabaseConnection();

            // Test 2: Admin Authentication
            System.out.println("\nTest 2: Admin Authentication");
            allTestsPassed &= testAdminAuth();

            // Test 3: Department Operations
            System.out.println("\nTest 3: Department Operations");
            allTestsPassed &= testDepartments();

            // Test 4: Employee Operations
            System.out.println("\nTest 4: Employee Operations");
            allTestsPassed &= testEmployees();

            // Test 5: Attendance Operations
            System.out.println("\nTest 5: Attendance Operations");
            allTestsPassed &= testAttendance();

            // Test 6: Payroll Operations
            System.out.println("\nTest 6: Payroll Operations");
            allTestsPassed &= testPayroll();

            // Test 7: Leave Request Operations
            System.out.println("\nTest 7: Leave Request Operations");
            allTestsPassed &= testLeaveRequests();

            // Summary
            System.out.println("\n========================================");
            if (allTestsPassed) {
                System.out.println("✅ ALL TESTS PASSED!");
                System.out.println("HRMS Application is working correctly.");
            } else {
                System.out.println("❌ SOME TESTS FAILED!");
                System.out.println("Please review the errors above.");
            }
            System.out.println("========================================");

        } catch (Exception e) {
            System.out.println("❌ Fatal error during testing: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static boolean testDatabaseConnection() {
        try {
            DBConnection.getConnection();
            System.out.println("✅ Database connection successful");
            return true;
        } catch (Exception e) {
            System.out.println("❌ Database connection failed: " + e.getMessage());
            return false;
        }
    }

    private static boolean testAdminAuth() {
        try {
            AdminDAO adminDAO = new AdminDAO();
            Admin admin = adminDAO.getAdminDetails("admin", "admin@123");
            
            if (admin != null) {
                System.out.println("✅ Admin authentication successful: " + admin.getUsername());
                return true;
            } else {
                System.out.println("❌ Admin authentication failed");
                return false;
            }
        } catch (Exception e) {
            System.out.println("❌ Admin authentication error: " + e.getMessage());
            return false;
        }
    }

    private static boolean testDepartments() {
        try {
            DepartmentDAO deptDAO = new DepartmentDAO();
            List<Department> departments = deptDAO.getAllDepartments();
            
            if (departments != null && departments.size() >= 4) {
                System.out.println("✅ Found " + departments.size() + " departments:");
                for (Department dept : departments) {
                    System.out.println("   - " + dept.getName() + " (Location: " + dept.getLocation() + ")");
                }
                return true;
            } else {
                System.out.println("❌ Expected at least 4 departments, found: " + 
                    (departments != null ? departments.size() : 0));
                return false;
            }
        } catch (Exception e) {
            System.out.println("❌ Department test error: " + e.getMessage());
            return false;
        }
    }

    private static boolean testEmployees() {
        try {
            EmployeeDAO empDAO = new EmployeeDAO();
            List<Employee> employees = empDAO.getAllEmployees();
            
            if (employees != null && employees.size() >= 8) {
                System.out.println("✅ Found " + employees.size() + " employees:");
                for (Employee emp : employees) {
                    System.out.println("   - " + emp.getName() + " (" + emp.getDepartmentName() + 
                        ") - Salary: $" + emp.getSalary());
                }
                
                // Test employee authentication
                Employee testEmp = empDAO.validateLogin(1, "pass123");
                if (testEmp != null) {
                    System.out.println("✅ Employee authentication successful: " + testEmp.getName());
                    return true;
                } else {
                    System.out.println("❌ Employee authentication failed");
                    return false;
                }
            } else {
                System.out.println("❌ Expected at least 8 employees, found: " + 
                    (employees != null ? employees.size() : 0));
                return false;
            }
        } catch (Exception e) {
            System.out.println("❌ Employee test error: " + e.getMessage());
            return false;
        }
    }

    private static boolean testAttendance() {
        try {
            AttendanceDAO attDAO = new AttendanceDAO();
            List<Attendance> allAttendance = attDAO.getAllAttendance();
            
            if (allAttendance != null && allAttendance.size() > 0) {
                System.out.println("✅ Found " + allAttendance.size() + " attendance records");
                
                // Test attendance for specific employee
                List<Attendance> empAttendance = attDAO.getAttendanceByEmployee(1);
                if (empAttendance != null && empAttendance.size() > 0) {
                    System.out.println("✅ Employee #1 has " + empAttendance.size() + " attendance records");
                    return true;
                } else {
                    System.out.println("❌ No attendance found for employee #1");
                    return false;
                }
            } else {
                System.out.println("❌ No attendance records found");
                return false;
            }
        } catch (Exception e) {
            System.out.println("❌ Attendance test error: " + e.getMessage());
            return false;
        }
    }

    private static boolean testPayroll() {
        try {
            PayrollDAO payrollDAO = new PayrollDAO();
            List<Payroll> allPayrolls = payrollDAO.getAllPayrolls();
            
            if (allPayrolls != null && allPayrolls.size() >= 8) {
                System.out.println("✅ Found " + allPayrolls.size() + " payroll records");
                
                // Test payroll for specific employee
                List<Payroll> empPayrolls = payrollDAO.getPayrollByEmployee(1);
                if (empPayrolls != null && empPayrolls.size() > 0) {
                    Payroll payroll = empPayrolls.get(0);
                    System.out.println("✅ Employee #1 payroll: Net Salary = $" + payroll.getNetSalary());
                    return true;
                } else {
                    System.out.println("❌ No payroll found for employee #1");
                    return false;
                }
            } else {
                System.out.println("❌ Expected at least 8 payroll records, found: " + 
                    (allPayrolls != null ? allPayrolls.size() : 0));
                return false;
            }
        } catch (Exception e) {
            System.out.println("❌ Payroll test error: " + e.getMessage());
            return false;
        }
    }

    private static boolean testLeaveRequests() {
        try {
            LeaveRequestDAO leaveDAO = new LeaveRequestDAO();
            List<LeaveRequest> allLeaves = leaveDAO.getAllLeaveRequestsDetailed();
            
            if (allLeaves != null && allLeaves.size() >= 4) {
                System.out.println("✅ Found " + allLeaves.size() + " leave requests:");
                int pending = 0, approved = 0, rejected = 0;
                
                for (LeaveRequest leave : allLeaves) {
                    System.out.println("   - Employee #" + leave.getEmployeeId() + 
                        " (" + leave.getStartDate() + " to " + leave.getEndDate() + 
                        ") - " + leave.getStatus());
                    
                    if ("Pending".equalsIgnoreCase(leave.getStatus())) pending++;
                    else if ("Approved".equalsIgnoreCase(leave.getStatus())) approved++;
                    else if ("Rejected".equalsIgnoreCase(leave.getStatus())) rejected++;
                }
                
                System.out.println("✅ Leave Status: Pending=" + pending + ", Approved=" + 
                    approved + ", Rejected=" + rejected);
                return true;
            } else {
                System.out.println("❌ Expected at least 4 leave requests, found: " + 
                    (allLeaves != null ? allLeaves.size() : 0));
                return false;
            }
        } catch (Exception e) {
            System.out.println("❌ Leave request test error: " + e.getMessage());
            return false;
        }
    }
}
