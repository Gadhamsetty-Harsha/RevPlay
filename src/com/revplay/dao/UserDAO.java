package com.revplay.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.revplay.util.DBConnection;

public class UserDAO {

	/* =====================================================
                         REGISTER USER
       ===================================================== */
    public boolean registerUser(String name, String email, String password, String securityQuestion, String securityAnswer) {

        String sql = "INSERT INTO users (name, email, password, security_question, security_answer) VALUES (?, ?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, password);
            ps.setString(4, securityQuestion);
            ps.setString(5, securityAnswer);

            int rows = ps.executeUpdate();
            System.out.println("✅ User Registered successfully!");
            return rows > 0;

        } catch (Exception e) {
            return false;
        }
    }

   
    /* =====================================================
                       LOGIN USER
       ===================================================== */
    public Integer loginUser(String email, String password) {

        String sql = "SELECT user_id FROM users WHERE email=? AND password=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, email);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("user_id");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /* =====================================================
                    CHANGE PASSWORD
       ===================================================== */
    public boolean resetPasswordUsingSecurityAnswer(
            String email,
            String securityAnswer,
            String newPassword) {

        String sql = " UPDATE users SET password = ? WHERE email = ? AND security_answer = ? ";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, newPassword);
            ps.setString(2, email);
            ps.setString(3, securityAnswer);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
