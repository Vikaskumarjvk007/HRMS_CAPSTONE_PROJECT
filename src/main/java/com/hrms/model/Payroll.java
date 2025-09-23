package com.hrms.model;

import java.time.LocalDate;

public class Payroll {
    private int id;
    private int employeeId;
    private double basic;
    private double hra;
    private double allowances; // DA or other allowances
    private double deductions;
    private double netSalary;
    private LocalDate payDate;

    // 🔹 Extra fields (from JOIN queries)
    private String employeeName;
    private String departmentName;


    // ✅ Full constructor (used when reading from DB with JOIN)
    public Payroll(int id, int employeeId, double basic, double hra, double allowances,
                   double deductions, double netSalary, LocalDate payDate,
                   String employeeName, String departmentName) {
        this.id = id;
        this.employeeId = employeeId;
        this.basic = basic;
        this.hra = hra;
        this.allowances = allowances;
        this.deductions = deductions;
        this.netSalary = netSalary;
        this.payDate = payDate;
        this.employeeName = employeeName;
        this.departmentName = departmentName;
    }


    public Payroll() {
        // empty constructor for testing / frameworks
    }


    // ✅ Constructor for creating new payroll (without JOIN info)
    public Payroll(int employeeId, double basic, double hra, double allowances,
                   double deductions, double netSalary, LocalDate payDate) {
        this.employeeId = employeeId;
        this.basic = basic;
        this.hra = hra;
        this.allowances = allowances;
        this.deductions = deductions;
        this.netSalary = netSalary;
        this.payDate = payDate;
    }

    // ✅ Constructor for DB reads without JOIN
    public Payroll(int id, int employeeId, double basic, double hra, double allowances,
                   double deductions, double netSalary, LocalDate payDate) {
        this(id, employeeId, basic, hra, allowances, deductions, netSalary, payDate, null, null);
    }

    // ✅ Getters & Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getEmployeeId() { return employeeId; }
    public void setEmployeeId(int employeeId) { this.employeeId = employeeId; }

    public double getBasic() { return basic; }
    public void setBasic(double basic) { this.basic = basic; }

    public double getHra() { return hra; }
    public void setHra(double hra) { this.hra = hra; }

    public double getAllowances() { return allowances; }
    public void setAllowances(double allowances) { this.allowances = allowances; }

    public double getDeductions() { return deductions; }
    public void setDeductions(double deductions) { this.deductions = deductions; }

    public double getNetSalary() { return netSalary; }
    public void setNetSalary(double netSalary) { this.netSalary = netSalary; }

    public LocalDate getPayDate() { return payDate; }
    public void setPayDate(LocalDate payDate) { this.payDate = payDate; }

    public String getEmployeeName() { return employeeName; }
    public void setEmployeeName(String employeeName) { this.employeeName = employeeName; }

    public String getDepartmentName() { return departmentName; }
    public void setDepartmentName(String departmentName) { this.departmentName = departmentName; }

    @Override
    public String toString() {
        return "Payroll{" +
                "id=" + id +
                ", employeeId=" + employeeId +
                ", employeeName='" + employeeName + '\'' +
                ", departmentName='" + departmentName + '\'' +
                ", basic=" + basic +
                ", hra=" + hra +
                ", allowances=" + allowances +
                ", deductions=" + deductions +
                ", netSalary=" + netSalary +
                ", payDate=" + payDate +
                '}';
    }
}
