package com.hrms.model.dao;

import com.hrms.model.Employee;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDAO {

    // 🔹 Add new employee (with password)
    public int addEmployee(Employee emp) {
        String sql = "INSERT INTO employees (name, department_id, salary, email, phone_number, date_of_joining, password) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, emp.getName());
            ps.setInt(2, emp.getDepartmentId());
            ps.setDouble(3, emp.getSalary());
            ps.setString(4, emp.getEmail());
            ps.setString(5, emp.getPhoneNumber());
            ps.setDate(6, Date.valueOf(emp.getDateOfJoining()));
            ps.setString(7, emp.getPassword());

            int rows = ps.executeUpdate();
            if (rows > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getInt(1); // return auto-generated employee id
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Error adding employee: " + e.getMessage());
        }
        return -1;
    }

    // 🔹 Validate login by employeeId + password
    public Employee validateLogin(int empId, String password) {
        String sql = "SELECT e.id, e.name, e.department_id, d.name AS department_name, " +
                "e.salary, e.email, e.phone_number, e.date_of_joining, e.password " +
                "FROM employees e JOIN departments d ON e.department_id = d.id " +
                "WHERE e.id=? AND e.password=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, empId);
            ps.setString(2, password);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Employee emp = new Employee(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getInt("department_id"),
                            rs.getDouble("salary"),
                            rs.getString("email"),
                            rs.getString("phone_number"),
                            rs.getDate("date_of_joining").toLocalDate(),
                            rs.getString("password")
                    );
                    emp.setDepartmentName(rs.getString("department_name"));
                    return emp;
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Error validating login: " + e.getMessage());
        }
        return null;
    }

    // 🔹 Fetch all employees
    public List<Employee> getAllEmployees() {
        List<Employee> employees = new ArrayList<>();
        String sql = "SELECT e.id, e.name, e.department_id, d.name AS department_name, " +
                "e.salary, e.email, e.phone_number, e.date_of_joining, e.password " +
                "FROM employees e JOIN departments d ON e.department_id = d.id ORDER BY e.id";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Employee emp = new Employee(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("department_id"),
                        rs.getDouble("salary"),
                        rs.getString("email"),
                        rs.getString("phone_number"),
                        rs.getDate("date_of_joining").toLocalDate(),
                        rs.getString("password")
                );
                emp.setDepartmentName(rs.getString("department_name"));
                employees.add(emp);
            }
        } catch (SQLException e) {
            System.err.println("❌ Error fetching employees: " + e.getMessage());
        }
        return employees;
    }

    // 🔹 Get employee by ID
    public Employee getEmployeeById(int id) {
        String sql = "SELECT e.id, e.name, e.department_id, d.name AS department_name, " +
                "e.salary, e.email, e.phone_number, e.date_of_joining, e.password " +
                "FROM employees e JOIN departments d ON e.department_id = d.id WHERE e.id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Employee emp = new Employee(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getInt("department_id"),
                            rs.getDouble("salary"),
                            rs.getString("email"),
                            rs.getString("phone_number"),
                            rs.getDate("date_of_joining").toLocalDate(),
                            rs.getString("password")
                    );
                    emp.setDepartmentName(rs.getString("department_name"));
                    return emp;
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Error fetching employee by ID: " + e.getMessage());
        }
        return null;
    }

    // 🔹 Update employee (including password)
    public boolean updateEmployee(Employee emp) {
        String sql = "UPDATE employees SET name=?, department_id=?, salary=?, email=?, phone_number=?, date_of_joining=?, password=? WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, emp.getName());
            ps.setInt(2, emp.getDepartmentId());
            ps.setDouble(3, emp.getSalary());
            ps.setString(4, emp.getEmail());
            ps.setString(5, emp.getPhoneNumber());
            ps.setDate(6, Date.valueOf(emp.getDateOfJoining()));
            ps.setString(7, emp.getPassword());
            ps.setInt(8, emp.getId());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("❌ Error updating employee: " + e.getMessage());
        }
        return false;
    }

    // 🔹 Delete employee
    public boolean deleteEmployee(int id) {
        String sql = "DELETE FROM employees WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("❌ Error deleting employee: " + e.getMessage());
        }
        return false;
    }

    // 🔹 Search employees by name
    public List<Employee> searchEmployeeByName(String name) {
        List<Employee> employees = new ArrayList<>();
        String sql = "SELECT e.id, e.name, e.department_id, d.name AS department_name, " +
                "e.salary, e.email, e.phone_number, e.date_of_joining, e.password " +
                "FROM employees e JOIN departments d ON e.department_id = d.id " +
                "WHERE LOWER(e.name) LIKE ? ORDER BY e.id";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + name.toLowerCase() + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Employee emp = new Employee(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getInt("department_id"),
                            rs.getDouble("salary"),
                            rs.getString("email"),
                            rs.getString("phone_number"),
                            rs.getDate("date_of_joining").toLocalDate(),
                            rs.getString("password")
                    );
                    emp.setDepartmentName(rs.getString("department_name"));
                    employees.add(emp);
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Error searching employees: " + e.getMessage());
        }
        return employees;
    }

    // 🔹 Get employees by department
    public List<Employee> getEmployeesByDepartment(int departmentId) {
        List<Employee> employees = new ArrayList<>();
        String sql = "SELECT e.id, e.name, e.department_id, d.name AS department_name, " +
                "e.salary, e.email, e.phone_number, e.date_of_joining, e.password " +
                "FROM employees e JOIN departments d ON e.department_id = d.id " +
                "WHERE e.department_id=? ORDER BY e.id";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, departmentId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Employee emp = new Employee(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getInt("department_id"),
                            rs.getDouble("salary"),
                            rs.getString("email"),
                            rs.getString("phone_number"),
                            rs.getDate("date_of_joining").toLocalDate(),
                            rs.getString("password")
                    );
                    emp.setDepartmentName(rs.getString("department_name"));
                    employees.add(emp);
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Error fetching employees by department: " + e.getMessage());
        }
        return employees;
    }
}
