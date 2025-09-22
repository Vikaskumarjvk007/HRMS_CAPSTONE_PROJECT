package com.hrms.model.dao;

import com.hrms.model.Department;
import com.hrms.model.Employee;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DepartmentDAO {

    // ✅ Get Next Continuous Department ID
    private int getNextDepartmentId() {
        String sql = "SELECT IFNULL(MAX(id), 0) + 1 AS nextId FROM departments";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt("nextId");
            }
        } catch (SQLException e) {
            System.err.println("❌ Error fetching next department id: " + e.getMessage());
        }
        return 1;
    }

    // ✅ Add Department (with continuous ID)
    public int addDepartment(Department dept) {
        String sql = "INSERT INTO departments (id, name, location) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            int nextId = getNextDepartmentId();
            stmt.setInt(1, nextId);
            stmt.setString(2, dept.getName().trim());
            stmt.setString(3, dept.getLocation().trim());

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                return nextId;
            }
        } catch (SQLException e) {
            System.err.println("❌ Error adding department: " + e.getMessage());
        }
        return -1;
    }

    // ✅ Seed Initial Departments
    public void seedDepartments() {
        addIfNotExists("HR", "Hyderabad");
        addIfNotExists("Finance", "Mumbai");
        addIfNotExists("IT", "Chennai");
    }

    private void addIfNotExists(String name, String location) {
        String sql = "SELECT id FROM departments WHERE name=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, name.trim());
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    addDepartment(new Department(name, location));
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Error seeding department " + name + ": " + e.getMessage());
        }
    }

    // ✅ Get Department By ID
    public Department getDepartmentById(int id) {
        String sql = "SELECT id, name, location FROM departments WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Department(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("location")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Error fetching department: " + e.getMessage());
        }
        return null;
    }

    // ✅ Update Department
    public boolean updateDepartment(int id, String name, String location) {
        String sql = "UPDATE departments SET name=?, location=? WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, name.trim());
            ps.setString(2, location.trim());
            ps.setInt(3, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("❌ Error updating department: " + e.getMessage());
        }
        return false;
    }

    // ✅ Delete Department & Reorder IDs
    public boolean deleteDepartment(int id) {
        String sql = "DELETE FROM departments WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            boolean deleted = ps.executeUpdate() > 0;

            if (deleted) {
                reorderIds(); // re-fix numbering after delete
            }

            return deleted;
        } catch (SQLException e) {
            System.err.println("❌ Error deleting department: " + e.getMessage());
        }
        return false;
    }

    // ✅ Reorder Department IDs after delete (1,2,3,4…)
    private void reorderIds() {
        String sql = "SET @count = 0; " +
                "UPDATE departments SET id = (@count := @count + 1) ORDER BY id;";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute("SET @count = 0");
            stmt.executeUpdate("UPDATE departments SET id = (@count := @count + 1) ORDER BY id");
        } catch (SQLException e) {
            System.err.println("❌ Error reordering department IDs: " + e.getMessage());
        }
    }

    // ✅ Fetch all departments
    public List<Department> getAllDepartments() {
        List<Department> departments = new ArrayList<>();
        String sql = "SELECT id, name, location FROM departments ORDER BY id";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                departments.add(new Department(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("location")
                ));
            }
        } catch (SQLException e) {
            System.err.println("❌ Error fetching all departments: " + e.getMessage());
        }
        return departments;
    }
}
