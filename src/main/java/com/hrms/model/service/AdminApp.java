package com.hrms.model.service;

import com.hrms.model.*;
import com.hrms.model.dao.*;
import java.time.LocalDate;
import java.util.*;
//git"C:\Program Files\Git\bin\git.exe"
public class AdminApp {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // ✅ DAOs & Services
        AdminDAO adminDAO = new AdminDAO();
        DepartmentDAO deptDAO = new DepartmentDAO();
        deptDAO.seedDepartments(); // preload some departments
        EmployeeDAO empDAO = new EmployeeDAO();
        AttendanceDAO attDAO = new AttendanceDAO();
        PayrollDAO payDAO = new PayrollDAO();
        DynamoDBService dynamoService = new DynamoDBService();
        PayrollService payrollService = new PayrollService();


            // ----------------- ADMIN LOGIN -----------------
            Admin admin = null;
            int attempts = 0;
            while (attempts < 3 && admin == null) {
                System.out.print("Enter Admin Username: ");
                String username = sc.nextLine();
                System.out.print("Enter Admin Password: ");
                String password = sc.nextLine();
                admin = adminDAO.authenticate(username, password);
                if (admin == null) {
                    attempts++;
                    if (attempts < 3) {
                        System.out.println("❌ Invalid admin credentials! Attempts left: " + (3 - attempts));
                    }
                }
            }
            if (admin == null) {
                System.out.println("🚫 Too many failed attempts! Try again later.");
                return;
            }

            System.out.println("✅ Welcome, Admin " + admin.getUsername() + "!");

            while (true) {
                System.out.println("\n=== ADMIN MENU ===");
                System.out.println("1. Department Management");
                System.out.println("2. Employee Management");
                System.out.println("3. Attendance Management");
                System.out.println("4. Payroll Management");
                System.out.println("5. Leave Management");
                System.out.println("0. Exit");
                System.out.print("Enter choice: ");
                int ch = sc.nextInt();
                sc.nextLine();

                switch (ch) {
                    case 1 -> departmentMenu(sc, deptDAO, empDAO);
                    case 2 -> employeeMenu(sc, empDAO);
                    case 3 -> attendanceMenu(sc, attDAO);
                    case 4 -> payrollMenu(sc, payrollService, payDAO);
                    case 5 -> leaveMenu(sc, dynamoService, attDAO); // ✅ FIXED
                    case 0 -> {
                        System.out.println("👋 Exiting...");
                        return;
                    }
                    default -> System.out.println("❌ Invalid choice!");
                }

            }


    }

    // ----------------- HELPER METHODS -----------------

    private static void printDepartmentTable(List<Department> departments) {
        if (departments == null || departments.isEmpty()) {
            System.out.println("❌ No departments found.");
            return;
        }

        System.out.printf("%-5s %-20s %-20s%n", "ID", "Name", "Location");
        System.out.println("--------------------------------------------------");

        for (Department d : departments) {
            System.out.printf("%-5d %-20s %-20s%n",
                    d.getId(), d.getName(), d.getLocation());
        }
    }

    private static void departmentMenu(Scanner sc, DepartmentDAO deptDAO, EmployeeDAO empDAO) {
        while (true) {
            System.out.println("\n-- Department Menu --");
            System.out.println("1. Add Department");
            System.out.println("2. View All Departments");
            System.out.println("3. View Employees in Department");
            System.out.println("4. Update Department");
            System.out.println("5. Delete Department");
            System.out.println("0. Back");
            System.out.print("Enter choice: ");

            int dChoice = sc.nextInt();
            sc.nextLine();

            switch (dChoice) {
                case 1 -> {
                    System.out.print("Enter Department Name: ");
                    String name = sc.nextLine();
                    System.out.print("Enter Location: ");
                    String loc = sc.nextLine();
                    int newId = deptDAO.addDepartment(new Department(name, loc));
                    System.out.println(newId > 0 ? "✅ Department added with ID: " + newId : "❌ Failed to add department.");
                }
                case 2 -> {
                    List<Department> departments = deptDAO.getAllDepartments();
                    if (departments.isEmpty()) System.out.println("❌ No departments found.");
                    else printDepartmentTable(departments);
                }
                case 3 -> {
                    System.out.print("Enter Department ID: ");
                    int deptId = sc.nextInt();
                    sc.nextLine();
                    List<Employee> employees = empDAO.getEmployeesByDepartment(deptId);
                    if (employees.isEmpty()) System.out.println("❌ No employees found in this department.");
                    else printEmployeeTable(employees);
                }
                case 4 -> {
                    System.out.print("Enter Department ID to update: ");
                    int id = sc.nextInt();
                    sc.nextLine();
                    Department d = deptDAO.getDepartmentById(id);
                    if (d != null) {
                        System.out.print("Enter New Name (" + d.getName() + "): ");
                        String name = sc.nextLine();
                        if (name.isEmpty()) name = d.getName();
                        System.out.print("Enter New Location (" + d.getLocation() + "): ");
                        String loc = sc.nextLine();
                        if (loc.isEmpty()) loc = d.getLocation();
                        boolean updated = deptDAO.updateDepartment(id, name, loc);
                        System.out.println(updated ? "✅ Department updated." : "❌ Failed to update.");
                    } else {
                        System.out.println("❌ Department not found.");
                    }
                }
                case 5 -> {
                    System.out.print("Enter Department ID to delete: ");
                    int id = sc.nextInt();
                    sc.nextLine();
                    boolean deleted = deptDAO.deleteDepartment(id);
                    System.out.println(deleted ? "✅ Department deleted." : "❌ Failed to delete.");
                }
                case 0 -> {
                    return;
                }
                default -> System.out.println("❌ Invalid choice. Try again.");
            }
        }
    }

    private static void employeeMenu(Scanner sc, EmployeeDAO empDAO) {
        while (true) {
            System.out.println("\n-- Employee Menu --");
            System.out.println("1. Add Employee");
            System.out.println("2. View Employees");
            System.out.println("3. Update Employee");
            System.out.println("4. Delete Employee");
            System.out.println("5. Search Employee by Name");
            System.out.println("0. Back");
            System.out.print("Enter choice: ");
            int eChoice = sc.nextInt();
            sc.nextLine();

            switch (eChoice) {
                case 1 -> {
                    System.out.print("Enter Employee Name: ");
                    String name = sc.nextLine();

                    System.out.print("Enter Department ID: ");
                    int deptId = sc.nextInt();

                    System.out.print("Enter Salary: ");
                    double salary = sc.nextDouble();
                    sc.nextLine();

                    System.out.print("Enter Email: ");
                    String email = sc.nextLine();

                    System.out.print("Enter Phone: ");
                    String phone = sc.nextLine();

                    System.out.print("Enter Date of Joining (YYYY-MM-DD): ");
                    LocalDate doj = LocalDate.parse(sc.nextLine());

                    System.out.print("Enter Password: ");
                    String password = sc.nextLine();

                    Employee e = new Employee(0, name, deptId, salary, email, phone, doj, password);
                    int newId = empDAO.addEmployee(e);
                    System.out.println(newId > 0 ? "✅ Employee added with ID: " + newId : "❌ Failed to add employee.");
                }
                case 2 -> printEmployeeTable(empDAO.getAllEmployees());

                case 3 -> {
                    System.out.print("Enter Employee ID to update: ");
                    int id = sc.nextInt();
                    sc.nextLine();

                    Employee e = empDAO.getEmployeeById(id);
                    if (e != null) {
                        System.out.print("Enter New Name (" + e.getName() + "): ");
                        String name = sc.nextLine();
                        if (!name.isEmpty()) e.setName(name);

                        System.out.print("Enter New Department ID (" + e.getDepartmentId() + "): ");
                        String dept = sc.nextLine();
                        if (!dept.isEmpty()) e.setDepartmentId(Integer.parseInt(dept));

                        System.out.print("Enter New Salary (" + e.getSalary() + "): ");
                        String sal = sc.nextLine();
                        if (!sal.isEmpty()) e.setSalary(Double.parseDouble(sal));

                        System.out.print("Enter New Email (" + e.getEmail() + "): ");
                        String email = sc.nextLine();
                        if (!email.isEmpty()) e.setEmail(email);

                        System.out.print("Enter New Phone (" + e.getPhoneNumber() + "): ");
                        String phone = sc.nextLine();
                        if (!phone.isEmpty()) e.setPhoneNumber(phone);

                        System.out.print("Enter New Date of Joining (" + e.getDateOfJoining() + "): ");
                        String doj = sc.nextLine();
                        if (!doj.isEmpty()) e.setDateOfJoining(LocalDate.parse(doj));

                        System.out.print("Enter New Password (leave blank to keep existing): ");
                        String password = sc.nextLine();
                        if (!password.isEmpty()) e.setPassword(password);

                        boolean updated = empDAO.updateEmployee(e);
                        System.out.println(updated ? "✅ Employee updated." : "❌ Update failed.");
                    } else {
                        System.out.println("❌ Employee not found!");
                    }
                }

                case 4 -> {
                    System.out.print("Enter Employee ID to delete: ");
                    int id = sc.nextInt();
                    sc.nextLine();
                    boolean deleted = empDAO.deleteEmployee(id);
                    System.out.println(deleted ? "✅ Employee deleted." : "❌ Delete failed.");
                }

                case 5 -> {
                    System.out.print("Enter Employee Name to search: ");
                    String name = sc.nextLine();
                    printEmployeeTable(empDAO.searchEmployeeByName(name));
                }

                case 0 -> {
                    return;
                }

                default -> System.out.println("❌ Invalid choice!");
            }
        }
    }


    private static void printEmployeeTable(List<Employee> employees) {
        if (employees == null || employees.isEmpty()) {
            System.out.println("No employees found.");
            return;
        }

        // Print table header
        System.out.printf("%-5s %-20s %-10s %-10s %-25s %-15s %-12s%n",
                "ID", "Name", "Dept ID", "Salary", "Email", "Phone", "Date of Joining");
        System.out.println("-------------------------------------------------------------------------------------------");

        // Print each employee (without password)
        for (Employee e : employees) {
            System.out.printf("%-5d %-20s %-10d %-10.2f %-25s %-15s %-12s%n",
                    e.getId(),
                    e.getName(),
                    e.getDepartmentId(),
                    e.getSalary(),
                    e.getEmail(),
                    e.getPhoneNumber(),
                    e.getDateOfJoining());
        }
    }



    private static void attendanceMenu(Scanner sc, AttendanceDAO attDAO) {
        while (true) {
            System.out.println("\n-- Attendance Menu --");
            System.out.println("1. Mark Attendance");
            System.out.println("2. View Attendance by Employee");
            System.out.println("3. View All Attendance");
            System.out.println("0. Back");
            System.out.print("Enter choice: ");
            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1 -> {
                    System.out.print("Enter Employee ID: ");
                    int empId = sc.nextInt();
                    sc.nextLine();
                    System.out.print("Enter Status (Present/Absent): ");
                    String status = sc.nextLine();
                    attDAO.addAttendance(new Attendance(0, empId, LocalDate.now(), status));
                    System.out.println("✅ Attendance marked.");
                }
                case 2 -> {
                    System.out.print("Enter Employee ID: ");
                    int empId = sc.nextInt();
                    sc.nextLine();
                    List<Attendance> records = attDAO.getAttendanceByEmployee(empId);
                    if (records.isEmpty()) System.out.println("❌ No attendance found.");
                    else records.forEach(System.out::println);
                }
                case 3 -> {
                    List<Attendance> all = attDAO.getAllAttendance();
                    if (all.isEmpty()) System.out.println("❌ No attendance records.");
                    else all.forEach(System.out::println);
                }
                case 0 -> {
                    return;
                }
                default -> System.out.println("❌ Invalid choice. Try again.");
            }
        }
    }

    private static void payrollMenu(Scanner sc, PayrollService payrollService, PayrollDAO payDAO) {
        while (true) {
            System.out.println("\n-- Payroll Menu --");
            System.out.println("1. Generate Payroll for Employee");
            System.out.println("2. View All Payrolls (Payslip View)");
            System.out.println("3. View All Payrolls (Detailed HR Report)");
            System.out.println("4. Generate Payroll for All Employees (Queue)");
            System.out.println("5. Update Payroll");
            System.out.println("6. Delete Payroll");
            System.out.println("0. Back");
            System.out.print("Enter choice: ");
            int pChoice = sc.nextInt();
            sc.nextLine();

            switch (pChoice) {
                case 1 -> {
                    System.out.print("Enter Employee ID: ");
                    int empId = sc.nextInt();
                    sc.nextLine();
                    payrollService.generatePayroll(empId); // ✅ will auto print payslip
                }
                case 2 -> {
                    // ✅ Classic Payslip View
                    List<Payroll> payrolls = payDAO.getAllPayrolls();
                    if (payrolls.isEmpty()) {
                        System.out.println("❌ No payroll records.");
                    } else {
                        for (Payroll p : payrolls) {
                            Employee emp = new EmployeeDAO().getEmployeeById(p.getEmployeeId());
                            if (emp != null) {
                                System.out.println("\n================== PAYSLIP ==================");
                                System.out.printf("%-15s: %s%n", "Employee ID", emp.getId());
                                System.out.printf("%-15s: %s%n", "Name", emp.getName());
                                System.out.printf("%-15s: %s%n", "Department ID", emp.getDepartmentId());
                                System.out.printf("%-15s: %s%n", "Pay Date", p.getPayDate());
                                System.out.println("---------------------------------------------");
                                System.out.printf("%-20s : %10.2f%n", "Basic Salary (Monthly)", p.getBasic());
                                System.out.printf("%-20s : %10.2f%n", "HRA (20%)", p.getHra());
                                System.out.printf("%-20s : %10.2f%n", "DA (10%)", p.getAllowances());
                                System.out.printf("%-20s : %10.2f%n", "Deductions (5%)", p.getDeductions());
                                System.out.println("---------------------------------------------");
                                System.out.printf("%-20s : %10.2f%n", "Net Salary (Monthly)", p.getNetSalary());
                                System.out.println("=============================================\n");
                            }
                        }
                    }
                }
                case 3 -> {
                    List<Payroll> payrollDetails = payDAO.getAllPayrollDetails();

                    if (payrollDetails.isEmpty()) {
                        System.out.println("❌ No payroll records found!");
                    } else {
                        System.out.println("\n— Payroll Report —");
                        payrollDetails.forEach(p -> {
                            System.out.printf(
                                    "ID:%d | EmpID:%d | Name:%s | Dept:%s | Net:%.2f | Date:%s%n",
                                    p.getId(), p.getEmployeeId(), p.getEmployeeName(),
                                    p.getDepartmentName(), p.getNetSalary(), p.getPayDate()
                            );
                        });
                    }
                }

                case 4 -> payrollService.generatePayrollForAll(); // ✅ uses Queue
                case 5 -> {
                    System.out.print("Enter Payroll ID to update: ");
                    int payrollId = sc.nextInt();
                    sc.nextLine();

                    Payroll existingPayroll = payDAO.getAllPayrolls()
                            .stream()
                            .filter(p -> p.getId() == payrollId)
                            .findFirst()
                            .orElse(null);

                    if (existingPayroll == null) {
                        System.out.println("❌ Payroll ID not found.");
                        break;
                    }

                    System.out.print("Enter new Basic Salary: ");
                    existingPayroll.setBasic(sc.nextDouble());
                    System.out.print("Enter new HRA: ");
                    existingPayroll.setHra(sc.nextDouble());
                    System.out.print("Enter new DA/Allowances: ");
                    existingPayroll.setAllowances(sc.nextDouble());
                    System.out.print("Enter new Deductions: ");
                    existingPayroll.setDeductions(sc.nextDouble());
                    sc.nextLine();

                    existingPayroll.setNetSalary(
                            existingPayroll.getBasic()
                                    + existingPayroll.getHra()
                                    + existingPayroll.getAllowances()
                                    - existingPayroll.getDeductions()
                    );

                    if (payDAO.updatePayroll(existingPayroll)) {
                        System.out.println("✅ Payroll updated successfully.");
                    } else {
                        System.out.println("❌ Failed to update payroll.");
                    }
                }
                case 6 -> {
                    System.out.print("Enter Payroll ID to delete: ");
                    int payrollId = sc.nextInt();
                    sc.nextLine();
                    if (payDAO.deletePayroll(payrollId)) {
                        System.out.println("✅ Payroll deleted successfully.");
                    } else {
                        System.out.println("❌ Failed to delete payroll or ID not found.");
                    }
                }
                case 0 -> {
                    return;
                }
                default -> System.out.println("❌ Invalid choice!");
            }
        }
    }


    // ----------------- LEAVE MENU -----------------
    private static void leaveMenu(Scanner sc, DynamoDBService dynamoService, AttendanceDAO attDAO) {
        while (true) {
            System.out.println("\n-- Leave Menu --");
            System.out.println("1. View All Leave Requests");
            System.out.println("2. View Pending Leave Requests");
            System.out.println("3. Approve/Reject by Employee ID");
            System.out.println("4. Delete Leave Request");
            System.out.println("0. Back");
            System.out.print("Enter choice: ");
            int lChoice = sc.nextInt();
            sc.nextLine();

            switch (lChoice) {
                case 1 -> {
                    List<LeaveRequest> all = dynamoService.getAllLeaveRequests();
                    if (all.isEmpty()) System.out.println("❌ No leave requests found.");
                    else all.forEach(System.out::println);
                }
                case 2 -> {
                    List<LeaveRequest> pending = dynamoService.getLeaveRequestsByStatus("PENDING");
                    if (pending.isEmpty()) System.out.println("❌ No pending leave requests.");
                    else pending.forEach(System.out::println);
                }
                case 3 -> {
                    List<LeaveRequest> pending = dynamoService.getLeaveRequestsByStatus("PENDING");
                    if (pending.isEmpty()) {
                        System.out.println("❌ No pending leave requests.");
                        break;
                    }
                    pending.forEach(System.out::println);

                    System.out.print("Enter Employee ID to approve/reject: ");
                    int empId = sc.nextInt();
                    sc.nextLine();

                    List<LeaveRequest> empLeaves = pending.stream()
                            .filter(lr -> lr.getEmployeeId() == empId)
                            .toList();

                    if (empLeaves.isEmpty()) {
                        System.out.println("❌ No pending requests for Employee ID " + empId);
                        break;
                    }

                    LeaveRequest selected;
                    if (empLeaves.size() > 1) {
                        for (int i = 0; i < empLeaves.size(); i++) {
                            LeaveRequest lr = empLeaves.get(i);
                            System.out.println((i + 1) + ". Start: " + lr.getStartDate()
                                    + " | End: " + lr.getEndDate()
                                    + " | Reason: " + lr.getReason());
                        }
                        System.out.print("Select request number: ");
                        int idx = sc.nextInt();
                        sc.nextLine();
                        if (idx < 1 || idx > empLeaves.size()) {
                            System.out.println("❌ Invalid choice!");
                            break;
                        }
                        selected = empLeaves.get(idx - 1);
                    } else {
                        selected = empLeaves.get(0);
                    }

                    System.out.print("Enter Status (APPROVED/REJECTED): ");
                    String status = sc.nextLine().trim().toUpperCase();
                    if (!status.equals("APPROVED") && !status.equals("REJECTED")) {
                        System.out.println("❌ Invalid status!");
                        break;
                    }

                    // ✅ Update leave status
                    dynamoService.updateLeaveStatus(empId, selected.getStartDate(), status);

                    // ✅ If approved → auto mark attendance as Leave
                    if (status.equals("APPROVED")) {
                        LocalDate current = selected.getStartDate();
                        while (!current.isAfter(selected.getEndDate())) {
                            attDAO.addAttendance(new Attendance(0, empId, current, "Leave"));
                            current = current.plusDays(1);
                        }
                        System.out.println("📌 Attendance updated with 'Leave' for Employee ID " + empId
                                + " from " + selected.getStartDate() + " to " + selected.getEndDate());
                    }

                    System.out.println("✅ Leave status updated to " + status
                            + " for Employee ID " + empId
                            + " (Start: " + selected.getStartDate() + ")");
                }
                case 4 -> {
                    List<LeaveRequest> all = dynamoService.getAllLeaveRequests();
                    if (all.isEmpty()) {
                        System.out.println("❌ No leave requests found.");
                        break;
                    }
                    all.forEach(System.out::println);
                    System.out.print("Enter Request ID to delete: ");
                    String reqId = sc.nextLine();
                    dynamoService.deleteLeaveRequest(reqId);
                }
                case 0 -> {
                    return;
                }
                default -> System.out.println("❌ Invalid choice!");
            }
        }
    }
}
