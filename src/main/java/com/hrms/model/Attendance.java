package com.hrms.model;

import lombok.NonNull;

import java.time.LocalDate;

public class Attendance {
    private int id;
    private int employeeId;
    private LocalDate day;
    private String status; // Present, Absent, Leave, etc.

    // Constructors
    public Attendance() {}
    public Attendance(@NonNull int id,@NonNull int employeeId,@NonNull LocalDate day,@NonNull String status) {
        this.id = id;
        this.employeeId = employeeId;
        this.day = day;
        this.status = status;
    }

    // Getters & Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getEmployeeId() { return employeeId; }
    public void setEmployeeId(int employeeId) { this.employeeId = employeeId; }

    public LocalDate getDay() { return day; }
    public void setDay(LocalDate day) { this.day = day; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    @Override
    public String toString() {
        return "Attendance{id=" + id + ", empId=" + employeeId +
                ", day=" + day + ", status='" + status + "'}";
    }
}
