package com.hrms.model.dao;

import com.hrms.model.LeaveRequest;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class LeaveRequestDAO {

    private final Connection conn;

    public LeaveRequestDAO() {
        try {
            conn = DBConnection.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException("❌ Failed to establish database connection!", e);
        }
    }

    // ➕ Add a leave request
    public boolean addLeaveRequest(LeaveRequest lr) {
        String sql = "INSERT INTO leave_requests (request_id, employee_id, start_date, end_date, reason, status) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, lr.getRequestId());  // ✅ store UUID requestId
            ps.setInt(2, lr.getEmployeeId());
            ps.setDate(3, Date.valueOf(lr.getStartDate()));
            ps.setDate(4, Date.valueOf(lr.getEndDate()));
            ps.setString(5, lr.getReason());
            ps.setString(6, lr.getStatus());
            ps.executeUpdate();
            System.out.println("✅ Leave request submitted with Request ID=" + lr.getRequestId());
            return true;
        } catch (SQLException e) {
            System.err.println("❌ Error adding leave request: " + e.getMessage());
            return false;
        }
    }

    // 📋 Get all leave requests with employee names
    public List<LeaveRequest> getAllLeaveRequestsDetailed() {
        List<LeaveRequest> list = new ArrayList<>();
        String sql = "SELECT l.request_id, l.employee_id, l.start_date, l.end_date, l.reason, l.status, e.name AS empName " +
                "FROM leave_requests l " +
                "JOIN employees e ON l.employee_id = e.id " +
                "ORDER BY l.start_date DESC";

        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                LeaveRequest lr = new LeaveRequest(
                        rs.getString("request_id"),  // ✅ new constructor with requestId
                        rs.getInt("employee_id"),
                        rs.getDate("start_date").toLocalDate(),
                        rs.getDate("end_date").toLocalDate(),
                        rs.getString("reason"),
                        rs.getString("status")
                );

                lr.setEmployeeName(rs.getString("empName"));
                list.add(lr);
            }
        } catch (SQLException e) {
            System.err.println("❌ Error fetching leave requests: " + e.getMessage());
        }
        return list;
    }

    // ✏️ Update leave status (and mark attendance if Approved)
    public void updateLeaveStatus(String requestId, String status) {
        String updateSql = "UPDATE leave_requests SET status = ? WHERE request_id = ?";
        String fetchSql = "SELECT employee_id, start_date, end_date FROM leave_requests WHERE request_id = ?";
        String insertAttendanceSql = "INSERT INTO attendance (employee_id, date, status) VALUES (?, ?, 'Leave')";

        try (PreparedStatement ps = conn.prepareStatement(updateSql)) {
            ps.setString(1, status);
            ps.setString(2, requestId);
            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println("✅ Leave request " + requestId + " updated to " + status);

                // If approved, mark attendance as Leave for the duration
                if ("Approved".equalsIgnoreCase(status)) {
                    try (PreparedStatement fetchPs = conn.prepareStatement(fetchSql)) {
                        fetchPs.setString(1, requestId);
                        ResultSet rs = fetchPs.executeQuery();

                        if (rs.next()) {
                            int empId = rs.getInt("employee_id");
                            LocalDate start = rs.getDate("start_date").toLocalDate();
                            LocalDate end = rs.getDate("end_date").toLocalDate();

                            try (PreparedStatement insertPs = conn.prepareStatement(insertAttendanceSql)) {
                                for (LocalDate date = start; !date.isAfter(end); date = date.plusDays(1)) {
                                    insertPs.setInt(1, empId);
                                    insertPs.setDate(2, Date.valueOf(date));
                                    insertPs.addBatch();
                                }
                                insertPs.executeBatch();
                                System.out.println("✅ Attendance marked as 'Leave' for employee " + empId + " from " + start + " to " + end);
                            }
                        }
                    }
                }

            } else {
                System.out.println("❌ Leave request not found!");
            }
        } catch (SQLException e) {
            System.err.println("❌ Error updating leave status: " + e.getMessage());
        }
    }

    // ❌ Delete leave request
    public void deleteLeaveRequest(String requestId) {
        String sql = "DELETE FROM leave_requests WHERE request_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, requestId);
            int rows = ps.executeUpdate();
            if (rows > 0) {
                System.out.println("✅ Leave request " + requestId + " deleted.");
            } else {
                System.out.println("❌ Leave request not found!");
            }
        } catch (SQLException e) {
            System.err.println("❌ Error deleting leave request: " + e.getMessage());
        }
    }
}
