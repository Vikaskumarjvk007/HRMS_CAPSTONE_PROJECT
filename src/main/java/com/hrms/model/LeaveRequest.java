package com.hrms.model;

import java.time.LocalDate;
import java.util.UUID;

public class LeaveRequest {
    private String requestId;      // ✅ Unique ID for each request
    private int employeeId;
    private LocalDate startDate;
    private LocalDate endDate;
    private String reason;
    private String status;         // APPROVED, REJECTED, PENDING
    private String employeeName;   // Optional for joins (not stored in DB)

    // ✅ Constructor (auto-generates requestId, default PENDING status)
    public LeaveRequest(int employeeId, LocalDate startDate, LocalDate endDate, String reason) {
        this.requestId = UUID.randomUUID().toString(); // generate unique request ID
        this.employeeId = employeeId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.reason = reason;
        this.status = "Pending";   // default status
    }

    // ✅ Full constructor (used when fetching from DB)
    public LeaveRequest(String requestId, int employeeId, LocalDate startDate, LocalDate endDate, String reason, String status) {
        this.requestId = requestId;
        this.employeeId = employeeId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.reason = reason;
        this.status = status;
    }

    // Getters & Setters
    public String getRequestId() { return requestId; }
    public void setRequestId(String requestId) { this.requestId = requestId; }

    public int getEmployeeId() { return employeeId; }
    public void setEmployeeId(int employeeId) { this.employeeId = employeeId; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getEmployeeName() { return employeeName; }
    public void setEmployeeName(String employeeName) { this.employeeName = employeeName; }

    // ✅ Helper method to calculate number of leave days
    public long getLeaveDays() {
        return java.time.temporal.ChronoUnit.DAYS.between(startDate, endDate) + 1;
    }

    @Override
    public String toString() {
        return String.format(
                "Request ID: %s | Employee ID: %d | Name: %s | Start: %s | End: %s | Days: %d | Reason: %s | Status: %s",
                requestId,
                employeeId,
                (employeeName != null ? employeeName : "N/A"),
                startDate,
                endDate,
                getLeaveDays(),
                reason,
                status
        );
    }
}
