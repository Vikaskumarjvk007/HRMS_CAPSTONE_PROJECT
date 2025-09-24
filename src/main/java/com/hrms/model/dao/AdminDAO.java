package com.hrms.model.dao;

import com.hrms.model.Admin;
import java.sql.*;

public class AdminDAO {

    // Authenticate and return Admin object if valid
    public Admin getAdminDetails(String username, String password) {
        String sql = "SELECT * FROM admins WHERE username = ? AND password = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Admin(
                            rs.getInt("id"),
                            rs.getString("username"),
                            rs.getString("password")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Error authenticating admin: " + e.getMessage());
        }
        return null; // login failed
    }
}
