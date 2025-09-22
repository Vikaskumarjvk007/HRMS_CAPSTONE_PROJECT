package com.hrms.model.dao;

import com.hrms.model.Attendance;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AttendanceDAO {

    // ✅ Add attendance
    public void addAttendance(Attendance a) {
        String sql = "INSERT INTO attendance (employee_id, date, status) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, a.getEmployeeId());
            ps.setDate(2, Date.valueOf(a.getDay()));
            ps.setString(3, a.getStatus());

            ps.executeUpdate();
            System.out.println("✅ Attendance marked for empId=" + a.getEmployeeId() +
                    " on " + a.getDay());
        } catch (SQLException e) {
            System.err.println("❌ Error marking attendance: " + e.getMessage());
        }
    }

    // ✅ Get attendance by employee
    public List<Attendance> getAttendanceByEmployee(int empId) {
        List<Attendance> list = new ArrayList<>();
        String sql = "SELECT * FROM attendance WHERE employee_id = ? ORDER BY date DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, empId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new Attendance(
                            rs.getInt("id"),
                            rs.getInt("employee_id"),
                            rs.getDate("date").toLocalDate(),
                            rs.getString("status")
                    ));
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Error fetching attendance: " + e.getMessage());
        }
        return list;
    }

    // ✅ Get all attendance records
    public List<Attendance> getAllAttendance() {
        List<Attendance> list = new ArrayList<>();
        String sql = "SELECT * FROM attendance ORDER BY date DESC";
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                list.add(new Attendance(
                        rs.getInt("id"),
                        rs.getInt("employee_id"),
                        rs.getDate("date").toLocalDate(),
                        rs.getString("status")
                ));
            }
        } catch (SQLException e) {
            System.err.println("❌ Error fetching all attendance: " + e.getMessage());
        }
        return list;
    }
}
