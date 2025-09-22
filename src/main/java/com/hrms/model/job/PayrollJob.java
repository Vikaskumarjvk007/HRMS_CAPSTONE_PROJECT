package com.hrms.model.job;

import com.hrms.model.service.PayrollService;

public class PayrollJob {
    public static void main(String[] args) {
        System.out.println("Starting scheduled payroll generation...");
        new PayrollService().generatePayrollForAll();
        System.out.println("Payroll generation finished.");
    }
}
