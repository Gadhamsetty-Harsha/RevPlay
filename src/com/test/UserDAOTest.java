package com.test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import com.revplay.dao.UserDAO;
import com.revplay.util.DBConnection;
import com.revplay.util.PasswordUtil;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserDAOTest {

    private static UserDAO userDAO;
    private static String testEmail = "testuser@revplay.com";
    private static String password = "Test@123";

    @BeforeAll
    static void setup() {
        userDAO = new UserDAO();
    }

    /* Reset DB before each test */
    @BeforeEach
    void resetUser() throws Exception {

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     "DELETE FROM users WHERE email=?")) {

            ps.setString(1, testEmail);
            ps.executeUpdate();
        }

        userDAO.registerUser("Test User", testEmail, PasswordUtil.hashPassword(password), "What is your favorite color?", "Black");
    }

    @Test
    @Order(1)
    void testLoginSuccess() {
        Integer id = userDAO.loginUser(testEmail,  PasswordUtil.hashPassword(password));
        assertNotNull(id);
    }

    @Test
    @Order(2)
    void testResetPasswordUsingSecurityAnswer() {

        boolean result = userDAO.resetPasswordUsingSecurityAnswer(
            testEmail,
            "Black", // security answer
            PasswordUtil.hashPassword("NewPass@123")
        );

        assertTrue(result);
    }

    @Test
    @Order(3)
    void testLoginWithNewPassword() {

        userDAO.resetPasswordUsingSecurityAnswer(
            testEmail,
            "Black",
            PasswordUtil.hashPassword("NewPass@123")
        );

        Integer id = userDAO.loginUser(
            testEmail,
            PasswordUtil.hashPassword("NewPass@123")
        );

        assertNotNull(id);
    }

}
