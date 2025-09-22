package com.hrms.model.dao;

import com.hrms.model.Payroll;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class PayrollDAO {

    /** CREATE */
    public void addPayroll(Payroll p) {
        String sql = """
            INSERT INTO payrolls
            (employee_id, basic, hra, allowances, deductions, net_salary, pay_date)
            VALUES (?, ?, ?, ?, ?, ?, ?)
            """;
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, p.getEmployeeId());
            ps.setDouble(2, p.getBasic());
            ps.setDouble(3, p.getHra());
            ps.setDouble(4, p.getAllowances());
            ps.setDouble(5, p.getDeductions());
            ps.setDouble(6, p.getNetSalary());
            ps.setDate(7, Date.valueOf(p.getPayDate()));

            ps.executeUpdate();
            System.out.println("✅ Payroll saved for empId=" + p.getEmployeeId());
        } catch (SQLException e) {
            System.err.println("❌ Error saving payroll: " + e.getMessage());
        }
    }

    /** READ – all payroll rows (no join) */
    public List<Payroll> getAllPayrolls() {
        List<Payroll> list = new ArrayList<>();
        String sql = "SELECT * FROM payrolls ORDER BY pay_date DESC";
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapResultSetToPayroll(rs));
            }
        } catch (SQLException e) {
            System.err.println("❌ Error fetching payrolls: " + e.getMessage());
        }
        return list;
    }

    /** READ – payrolls with employee + department details */
    public List<Payroll> getAllPayrollDetails() {
        List<Payroll> list = new ArrayList<>();
        String sql = """
            SELECT p.id, p.employee_id,
                   p.basic, p.hra, p.allowances, p.deductions, p.net_salary, p.pay_date,
                   e.name AS emp_name, d.name AS dept_name
            FROM payrolls p
            JOIN employees e ON p.employee_id = e.id
            JOIN departments d ON e.department_id = d.id
            ORDER BY p.pay_date DESC
            """;
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapResultSetToPayrollWithJoin(rs));
            }
        } catch (SQLException e) {
            System.err.println("❌ Error fetching payrolls with details: " + e.getMessage());
        }
        return list;
    }

    /** READ – payrolls for a specific employee */
    public List<Payroll> getPayrollByEmployee(int empId) {
        List<Payroll> list = new ArrayList<>();
        String sql = "SELECT * FROM payrolls WHERE employee_id = ? ORDER BY pay_date DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, empId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToPayroll(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Error fetching payrolls for empId=" + empId + ": " + e.getMessage());
        }
        return list;
    }

    /** UPDATE */
    public boolean updatePayroll(Payroll p) {
        String sql = """
            UPDATE payrolls
            SET basic=?, hra=?, allowances=?, deductions=?, net_salary=?, pay_date=?
            WHERE id=?
            """;
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDouble(1, p.getBasic());
            ps.setDouble(2, p.getHra());
            ps.setDouble(3, p.getAllowances());
            ps.setDouble(4, p.getDeductions());
            ps.setDouble(5, p.getNetSalary());
            ps.setDate(6, Date.valueOf(p.getPayDate()));
            ps.setInt(7, p.getId());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("❌ Error updating payroll id=" + p.getId() + ": " + e.getMessage());
            return false;
        }
    }

    /** DELETE */
    public boolean deletePayroll(int id) {
        String sql = "DELETE FROM payrolls WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("❌ Error deleting payroll id=" + id + ": " + e.getMessage());
            return false;
        }
    }

    /** Queue-based batch insert */
    public void processPayrollQueue(List<Payroll> payrollList) {
        if (payrollList.isEmpty()) {
            System.out.println("❌ No payrolls to process.");
            return;
        }
        Queue<Payroll> q = new LinkedList<>(payrollList);
        System.out.println("⏳ Starting queue-based payroll processing...");
        while (!q.isEmpty()) {
            Payroll p = q.poll();
            try {
                addPayroll(p);
                System.out.println("✅ Payroll processed for empId=" + p.getEmployeeId());
            } catch (Exception e) {
                System.err.println("❌ Error processing payroll for empId=" + p.getEmployeeId() + ": " + e.getMessage());
            }
        }
        System.out.println("✅ Queue-based payroll processing completed.");
    }

    /* ---------- Private mapping helpers ---------- */
    private Payroll mapResultSetToPayroll(ResultSet rs) throws SQLException {
        return new Payroll(
                rs.getInt("id"),
                rs.getInt("employee_id"),
                rs.getDouble("basic"),
                rs.getDouble("hra"),
                rs.getDouble("allowances"),
                rs.getDouble("deductions"),
                rs.getDouble("net_salary"),
                rs.getDate("pay_date").toLocalDate()
        );
    }

    private Payroll mapResultSetToPayrollWithJoin(ResultSet rs) throws SQLException {
        return new Payroll(
                rs.getInt("id"),
                rs.getInt("employee_id"),
                rs.getDouble("basic"),
                rs.getDouble("hra"),
                rs.getDouble("allowances"),
                rs.getDouble("deductions"),
                rs.getDouble("net_salary"),
                rs.getDate("pay_date").toLocalDate(),
                rs.getString("emp_name"),
                rs.getString("dept_name")
        );
    }
}
