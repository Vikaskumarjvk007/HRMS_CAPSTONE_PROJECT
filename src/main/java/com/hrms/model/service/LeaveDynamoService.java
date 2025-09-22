package com.hrms.model.service;

import com.hrms.model.LeaveRequest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Stub service for Leave Management (simulating DynamoDB with in-memory list).
 */
public class LeaveDynamoService {

    private final List<LeaveRequest> leaveRequests = new ArrayList<>();

    public LeaveDynamoService() {
        // ✅ Seed with sample data with requestId
        leaveRequests.add(new LeaveRequest(
                UUID.randomUUID().toString(),
                1,
                LocalDate.now(),
                LocalDate.now().plusDays(2),
                "Vacation",
                "PENDING"
        ));

        leaveRequests.add(new LeaveRequest(
                UUID.randomUUID().toString(),
                2,
                LocalDate.now().plusDays(3),
                LocalDate.now().plusDays(5),
                "Medical",
                "PENDING"
        ));
    }

    /** ➕ Add a new leave request */
    public void addLeaveRequest(LeaveRequest req) {
        // Generate requestId if not already set
        if (req.getRequestId() == null || req.getRequestId().isEmpty()) {
            req.setRequestId(UUID.randomUUID().toString());
        }
        leaveRequests.add(req);
    }

    /** 📋 Get all leave requests */
    public List<LeaveRequest> getAllLeaveRequests() {
        return new ArrayList<>(leaveRequests);
    }

    /** 📋 Get leave requests by employee ID */
    public List<LeaveRequest> getLeaveRequestsByEmployee(int empId) {
        List<LeaveRequest> result = new ArrayList<>();
        for (LeaveRequest lr : leaveRequests) {
            if (lr.getEmployeeId() == empId) {
                result.add(lr);
            }
        }
        return result;
    }

    /** ✏️ Update leave status by requestId */
    public void updateLeaveStatus(String requestId, String newStatus) {
        for (LeaveRequest lr : leaveRequests) {
            if (lr.getRequestId().equals(requestId)) {
                lr.setStatus(newStatus);
                break;
            }
        }
    }

    /** ❌ Delete a leave request by requestId */
    public void deleteLeaveRequest(String requestId) {
        leaveRequests.removeIf(lr -> lr.getRequestId().equals(requestId));
    }

    /** 📋 Filter leave requests by status */
    public List<LeaveRequest> getLeaveRequestsByStatus(String status) {
        List<LeaveRequest> result = new ArrayList<>();
        for (LeaveRequest lr : leaveRequests) {
            if (lr.getStatus().equalsIgnoreCase(status)) {
                result.add(lr);
            }
        }
        return result;
    }
}
