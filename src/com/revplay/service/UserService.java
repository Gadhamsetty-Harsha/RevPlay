package com.revplay.service;

import com.revplay.dao.UserDAO;
import com.revplay.util.PasswordUtil;
import com.revplay.util.ValidationUtil;

public class UserService {

    private UserDAO userDAO = new UserDAO();

    /* =====================================================
                           REGISTER
       ===================================================== */
    public void register(String name, String email, String password, String securityQuestion, String securityAnswer) {

        email = email.trim().toLowerCase();

        if (!ValidationUtil.isValidEmail(email)) {
            System.out.println("❌ Invalid email format!");
            return;
        }

        if (!ValidationUtil.isStrongPassword(password)) {
            System.out.println("❌ Weak password!");
            System.out.println("Password must contain uppercase, lowercase, digit & min 8 chars");
            return;
        }

        userDAO.registerUser(name, email, password, securityQuestion, securityAnswer);
    }

    /* =====================================================
                         LOGIN
       ===================================================== */
    public Integer login(String email, String password) {

        Integer userId = userDAO.loginUser(email, password);

        if (userId != null) {
            System.out.println("✅ Login successful!");
            return userId;
        } else {
            System.out.println("❌ Invalid email or password!");
            return null;
        }
    }

    /* =====================================================
                         CHANGE PASSWORD
       ===================================================== */
    public void forgotPassword(String email, String securityAnswer, String newPassword) {

        if (!ValidationUtil.isStrongPassword(newPassword)) {
            System.out.println("❌ Invalid password format!");
            return;
        }

        boolean updated = userDAO.resetPasswordUsingSecurityAnswer(
                email,
                securityAnswer,
                PasswordUtil.hashPassword(newPassword)
        );

        if (updated) {
            System.out.println("✅ Password reset successful!");
        } else {
            System.out.println("❌ Security answer incorrect!");
        }
    }
}
