package com.hrms.model.test;

import com.hrms.model.Payroll;
import com.hrms.model.dao.PayrollDAO;
import com.hrms.model.service.PayrollService;
import org.junit.jupiter.api.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PayrollServiceTest {

    private PayrollService payrollService;
    private PayrollDAO payrollDAO;
    private static final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private static final PrintStream originalOut = System.out;

    @BeforeAll
    static void redirectConsole() {
        System.setOut(new PrintStream(outContent));
    }

    @BeforeEach
    void init() {
        payrollService = new PayrollService();
        payrollDAO     = new PayrollDAO();
        outContent.reset();
    }

    @AfterAll
    static void restoreStreams() {
        System.setOut(originalOut);
    }

    /**
     * 1️⃣ Happy-path test: Employee 3 (Aravind) payroll generation and DB check.
     */
    @Test
    void testGeneratePayrollForAravind() {
        payrollService.generatePayroll(3);

        List<Payroll> payrolls = payrollDAO.getPayrollByEmployee(3);
        assertFalse(payrolls.isEmpty(), "Payroll should be generated for employee ID=3");
        Payroll p = payrolls.get(0);

        double annualCtc    = 300000.0;
        double monthlyBasic = annualCtc / 12.0;
        double hra          = monthlyBasic * 0.20;
        double allowances   = monthlyBasic * 0.10;
        double deductions   = monthlyBasic * 0.05;
        double netSalary    = monthlyBasic + hra + allowances - deductions;

        assertEquals(monthlyBasic, p.getBasic(),      0.01);
        assertEquals(hra,         p.getHra(),        0.01);
        assertEquals(allowances,  p.getAllowances(), 0.01);
        assertEquals(deductions,  p.getDeductions(), 0.01);
        assertEquals(netSalary,   p.getNetSalary(),  0.01);

        String output = outContent.toString();
        assertTrue(output.contains("Aravind"));
        assertTrue(output.contains("31,250.00"));
    }

    /**
     * 2️⃣ Invalid employee ID should not create a payroll record.
     */
    @Test
    void testGeneratePayrollWithInvalidEmployee() {
        payrollService.generatePayroll(99999);
        List<Payroll> payrolls = payrollDAO.getPayrollByEmployee(99999);
        assertTrue(payrolls.isEmpty(), "No payroll should be generated for invalid employee");
        assertTrue(outContent.toString().contains("Employee not found"),
                "Console should mention employee not found");
    }

    /**
     * 3️⃣ Generating payroll for all employees should create at least one record.
     */
    @Test
    void testGeneratePayrollForAllEmployees() {
        payrollService.generatePayrollForAll();
        // After running, there must be at least one payroll record in the DB
        List<Payroll> all = payrollDAO.getAllPayrolls();
        assertFalse(all.isEmpty(), "Payroll records should exist after generatePayrollForAll");
        assertTrue(outContent.toString().contains("Payroll generated for all employees"),
                "Console should confirm generation for all employees");
    }

    /**
     * 4️⃣ Multiple calls for the same employee should create multiple payroll records (new pay run).
     */
    @Test
    void testMultiplePayrollRunsCreateMultipleRecords() {
        int empId = 3;
        int beforeCount = payrollDAO.getPayrollByEmployee(empId).size();

        payrollService.generatePayroll(empId);
        payrollService.generatePayroll(empId);

        int afterCount = payrollDAO.getPayrollByEmployee(empId).size();
        assertTrue(afterCount >= beforeCount + 2,
                "Two additional payroll records should be created for employee " + empId);
    }

    /**
     * 5️⃣ Verify that printed payslip contains today’s date.
     */
    @Test
    void testPayslipContainsCurrentDate() {
        payrollService.generatePayroll(3);
        String output = outContent.toString();
        assertTrue(output.contains(LocalDate.now().toString()),
                "Payslip should include the current date");
    }
}
