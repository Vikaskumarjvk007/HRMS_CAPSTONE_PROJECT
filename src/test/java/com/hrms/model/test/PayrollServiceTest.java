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

    @Test
    void testGeneratePayrollForEmployee() {
        payrollService.generatePayroll(3);
        List<Payroll> payrolls = payrollDAO.getPayrollByEmployee(3);
        assertFalse(payrolls.isEmpty());
        Payroll p = payrolls.get(0);

        double monthlyBasic = 300000.0 / 12.0;
        double hra          = monthlyBasic * 0.20;
        double allowances   = monthlyBasic * 0.10;
        double deductions   = monthlyBasic * 0.05;
        double netSalary    = monthlyBasic + hra + allowances - deductions;

        assertEquals(monthlyBasic, p.getBasic(), 0.01);
        assertEquals(hra, p.getHra(), 0.01);
        assertEquals(allowances, p.getAllowances(), 0.01);
        assertEquals(deductions, p.getDeductions(), 0.01);
        assertEquals(netSalary, p.getNetSalary(), 0.01);

        String output = outContent.toString();
        assertTrue(output.contains("empId=3"));
    }

    @Test
    void testGeneratePayrollForInvalidEmployee() {
        payrollService.generatePayroll(99999);
        List<Payroll> payrolls = payrollDAO.getPayrollByEmployee(99999);
        assertTrue(payrolls.isEmpty());
        assertTrue(outContent.toString().contains("Employee not found"));
    }

    @Test
    void testGeneratePayrollForAllEmployees() {
        payrollService.generatePayrollForAll();
        List<Payroll> all = payrollDAO.getAllPayrolls();
        assertFalse(all.isEmpty());
        assertTrue(outContent.toString().contains("Payroll generated for all employees"));
    }

    @Test
    void testMultiplePayrollRuns() {
        int empId = 3;
        int beforeCount = payrollDAO.getPayrollByEmployee(empId).size();

        payrollService.generatePayroll(empId);
        payrollService.generatePayroll(empId);

        int afterCount = payrollDAO.getPayrollByEmployee(empId).size();
        assertTrue(afterCount >= beforeCount + 2);
    }

    @Test
    void testPayslipContainsCurrentDate() {
        payrollService.generatePayroll(3);
        String output = outContent.toString();
        assertTrue(output.contains(LocalDate.now().toString()));
    }
}
