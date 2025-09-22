package com.hrms.model;



import java.time.LocalDate;

public class Employee {
    private int id;
    private String name;
    private int departmentId;
    private double salary;
    private String email;
    private String phoneNumber;
    private LocalDate dateOfJoining;
    private String departmentName;

    // ✅ New field for login
    private String password;

    // Full constructor
    public Employee(int id, String name, int departmentId, double salary, String email,
                    String phoneNumber, LocalDate dateOfJoining, String password) {
        this.id = id;
        this.name = name;
        this.departmentId = departmentId;
        this.salary = salary;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.dateOfJoining = dateOfJoining;
        this.password = password;
    }

    // Existing constructor (without password, for legacy use)
    public Employee(int id, String name, int departmentId, double salary, String email,
                    String phoneNumber, LocalDate dateOfJoining) {
        this(id, name, departmentId, salary, email, phoneNumber, dateOfJoining, null);
    }

    public Employee() {}

    // ✅ Getters & Setters
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getDepartmentId() { return departmentId; }
    public void setDepartmentId(int departmentId) { this.departmentId = departmentId; }

    public double getSalary() { return salary; }
    public void setSalary(double salary) { this.salary = salary; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public LocalDate getDateOfJoining() { return dateOfJoining; }
    public void setDateOfJoining(LocalDate dateOfJoining) { this.dateOfJoining = dateOfJoining; }

    public String getDepartmentName() { return departmentName; }
    public void setDepartmentName(String departmentName) { this.departmentName = departmentName; }

    @Override
    public String toString() {
        return String.format("Employee{id=%d, name='%s', deptId=%d, salary=%.2f, email='%s', phone='%s', joinDate=%s, deptName='%s'}",
                id, name, departmentId, salary, email, phoneNumber, dateOfJoining, departmentName);
    }
}
