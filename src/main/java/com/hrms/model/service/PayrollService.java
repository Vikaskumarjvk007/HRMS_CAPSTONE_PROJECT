package com.hrms.model.service;

import com.hrms.model.Employee;
import com.hrms.model.Payroll;
import com.hrms.model.dao.EmployeeDAO;
import com.hrms.model.dao.PayrollDAO;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Handles payroll generation and payslip printing.
 */
public class PayrollService {
    private final EmployeeDAO empDAO = new EmployeeDAO();
    private final PayrollDAO payDAO = new PayrollDAO();

    // Format like 50,000.00
    private final DecimalFormat moneyFormat = new DecimalFormat("#,##0.00");

    /**
     * Generate payroll for a single employee using today's date.
     */
    public void generatePayroll(int empId) {
        generatePayroll(empId, LocalDate.now());
    }

    /**
     * Generate payroll for a single employee with a specific pay date.
     */
    public void generatePayroll(int empId, LocalDate payDate) {
        Employee emp = empDAO.getEmployeeById(empId);
        if (emp == null) {
            System.out.println("❌ Employee not found!");
            return;
        }

        double annualSalary = emp.getSalary();
        double monthlyBasic = annualSalary / 12;

        double hra = monthlyBasic * 0.20;        // 20% HRA
        double da  = monthlyBasic * 0.10;        // 10% DA or allowances
        double deductions = monthlyBasic * 0.05; // 5% deductions
        double netSalary  = monthlyBasic + hra + da - deductions;

        // Save to DB (monthly figures)
        Payroll payroll = new Payroll(
                empId,
                monthlyBasic,
                hra,
                da,           // allowances column
                deductions,
                netSalary,
                payDate
        );
        payDAO.addPayroll(payroll);

        printPayslip(emp, annualSalary, monthlyBasic, hra, da, deductions, netSalary, payDate);
    }

    /**
     * Generate payroll for all employees using a FIFO queue (today's date).
     */
    public void generatePayrollForAll() {
        generatePayrollForAll(LocalDate.now());
    }

    /**
     * Generate payroll for all employees using a FIFO queue with a custom date.
     */
    public void generatePayrollForAll(LocalDate payDate) {
        List<Employee> employees = empDAO.getAllEmployees();
        if (employees.isEmpty()) {
            System.out.println("❌ No employees found!");
            return;
        }

        Queue<Employee> queue = new LinkedList<>(employees);
        while (!queue.isEmpty()) {
            generatePayroll(queue.poll().getId(), payDate);
        }

        System.out.println("✅ Payroll generated for all employees (queue-based).");
    }

    /**
     * Batch insert payrolls using the DAO's queue method
     */
    public void processPayrollBatch(List<Payroll> payrollList) {
        payDAO.processPayrollQueue(payrollList);
    }

    /**
     * Print formatted payslip to console.
     */
    private void printPayslip(Employee emp,
                              double annualSalary,
                              double monthlyBasic,
                              double hra,
                              double da,
                              double deductions,
                              double netSalary,
                              LocalDate payDate) {

        System.out.println("\n================== PAYSLIP ==================");
        System.out.printf("%-15s: %s%n", "Employee ID", emp.getId());
        System.out.printf("%-15s: %s%n", "Name", emp.getName());
        System.out.printf("%-15s: %s%n", "Department ID", emp.getDepartmentId());
        System.out.printf("%-15s: %s%n", "Pay Date", payDate);
        System.out.println("---------------------------------------------");
        System.out.printf("%-20s : %10s%n", "Annual CTC", moneyFormat.format(annualSalary));
        System.out.println("---------------------------------------------");
        System.out.printf("%-20s : %10s%n", "Basic Salary (Monthly)", moneyFormat.format(monthlyBasic));
        System.out.printf("%-20s : %10s%n", "HRA (20%)", moneyFormat.format(hra));
        System.out.printf("%-20s : %10s%n", "DA (10%)", moneyFormat.format(da));
        System.out.printf("%-20s : %10s%n", "Deductions (5%)", moneyFormat.format(deductions));
        System.out.println("---------------------------------------------");
        System.out.printf("%-20s : %10s%n", "Net Salary (Monthly)", moneyFormat.format(netSalary));
        System.out.println("=============================================\n");
    }
}
