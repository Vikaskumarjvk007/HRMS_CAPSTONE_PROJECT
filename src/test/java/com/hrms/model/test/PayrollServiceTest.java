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

    private static final int TEST_EMPLOYEE_ID = 3;
    private static final double TEST_ANNUAL_SALARY = 300_000.0;

    private static final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private static final PrintStream originalOut = System.out;

    @BeforeAll
    static void redirectConsole() {
        System.setOut(new PrintStream(outContent));
    }

    @BeforeEach
    void init() {
        payrollService = new PayrollService();
        payrollDAO = new PayrollDAO();
        outContent.reset();

        // Clean up previous test payrolls for consistent test results
        List<Payroll> existingPayrolls = payrollDAO.getPayrollByEmployee(TEST_EMPLOYEE_ID);
        for (Payroll p : existingPayrolls) {
            payrollDAO.deletePayroll(p.getId());
        }
    }

    @AfterAll
    static void restoreStreams() {
        System.setOut(originalOut);
    }

    @Test
    @DisplayName("Generate payroll for a valid employee")
    void testGeneratePayrollForEmployee() {
        payrollService.generatePayroll(TEST_EMPLOYEE_ID);
        List<Payroll> payrolls = payrollDAO.getPayrollByEmployee(TEST_EMPLOYEE_ID);
        assertFalse(payrolls.isEmpty(), "Payroll list should not be empty for a valid employee");

        Payroll p = payrolls.get(0);

        double monthlyBasic = TEST_ANNUAL_SALARY / 12;
        double hra = monthlyBasic * 0.20;
        double allowances = monthlyBasic * 0.10;
        double deductions = monthlyBasic * 0.05;
        double netSalary = monthlyBasic + hra + allowances - deductions;

        assertEquals(monthlyBasic, p.getBasic(), 0.01, "Basic salary mismatch");
        assertEquals(hra, p.getHra(), 0.01, "HRA mismatch");
        assertEquals(allowances, p.getAllowances(), 0.01, "Allowances mismatch");
        assertEquals(deductions, p.getDeductions(), 0.01, "Deductions mismatch");
        assertEquals(netSalary, p.getNetSalary(), 0.01, "Net salary mismatch");

        String output = outContent.toString();
        assertTrue(output.contains("empId=" + TEST_EMPLOYEE_ID), "Output should contain employee ID");
    }

    @Test
    @DisplayName("Generate payroll for invalid employee")
    void testGeneratePayrollForInvalidEmployee() {
        int invalidEmpId = 99999;
        payrollService.generatePayroll(invalidEmpId);

        List<Payroll> payrolls = payrollDAO.getPayrollByEmployee(invalidEmpId);
        assertTrue(payrolls.isEmpty(), "Payroll should be empty for invalid employee");

        assertTrue(outContent.toString().contains("Employee not found"), "Console should print 'Employee not found'");
    }

    @Test
    @DisplayName("Generate payroll for all employees")
    void testGeneratePayrollForAllEmployees() {
        payrollService.generatePayrollForAll();
        List<Payroll> allPayrolls = payrollDAO.getAllPayrolls();
        assertFalse(allPayrolls.isEmpty(), "Payroll list should not be empty after generating for all employees");

        assertTrue(outContent.toString().contains("Payroll generated for all employees"),
                "Console should indicate payroll generation for all employees");
    }

    @Test
    @DisplayName("Multiple payroll runs for single employee")
    void testMultiplePayrollRuns() {
        int beforeCount = payrollDAO.getPayrollByEmployee(TEST_EMPLOYEE_ID).size();

        payrollService.generatePayroll(TEST_EMPLOYEE_ID);
        payrollService.generatePayroll(TEST_EMPLOYEE_ID);

        int afterCount = payrollDAO.getPayrollByEmployee(TEST_EMPLOYEE_ID).size();
        assertEquals(beforeCount + 2, afterCount,
                "Two additional payroll records should be created for employee " + TEST_EMPLOYEE_ID);
    }

    @Test
    @DisplayName("Payslip contains current date")
    void testPayslipContainsCurrentDate() {
        payrollService.generatePayroll(TEST_EMPLOYEE_ID);
        String output = outContent.toString();

        assertTrue(output.contains(LocalDate.now().toString()),
                "Payslip output should contain today's date");
    }
}
