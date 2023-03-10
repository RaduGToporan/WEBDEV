package com.green.marketplace.user;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.*;
import java.util.UUID;

@Controller
public class UserController {

    @Autowired
    private Codec codec;

    @GetMapping("login")
    public String login() {
        return "user/login";
    }

    @GetMapping("profile")
    public String profile(@CookieValue(value = "sessionID", required = false, defaultValue="-1") String sessionIDCookie, Model model) {
        int uid = idOfSession(sessionIDCookie);

		// If not logged in, go back to login
		if (uid == -1) {
            return login();
        }

        // Get user's orders

        model.addAttribute("page", "Profile");
        return "user/profile";
    }

    @PostMapping("login")
    public String login(@CookieValue(value = "sessionID", required = false) String sessionIDCookie, HttpServletResponse res, @RequestParam(defaultValue = "user") String username, @RequestParam String password, Model model) {
        if (username.equals("admin") && password.equals("ai_marketplace")) {
            deleteSession(sessionIDCookie, res);
            model.addAttribute("page", "Dashboard");
            return "user/dashboard";
        } else if (!username.equals("user")) {
            try {
                Connection conn = getConnection();
                ResultSet rs = conn.prepareStatement("SELECT * FROM marketplace.users").executeQuery();

                while (rs.next()) {
                    String rsName = rs.getString("username");
                    String rsPass = rs.getString("password");

                    if (username.equals(rsName) && codec.getEncoder().matches(password, rsPass)) {
                        deleteSession(sessionIDCookie, res);
                        Cookie cookie = createSessionCookie(res);
                        String query = String.format("UPDATE marketplace.users SET sessionID='%s' WHERE username='%s' AND password='%s'", cookie.getValue(), rsName, rsPass);
                        conn.prepareStatement(query).execute();
                        res.addCookie(cookie);

                        return profile(cookie.getValue(), model);
                    }
                }

                conn.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        return "user/login";
    }

    @GetMapping("signup")
    public String signup() {
        return "user/signup";
    }

    /*
     * Unique ID is prone to error if we allow deletion of accounts but isn't a requirement, so it's fine
     * */
    @PostMapping("signup")
    public String signupPost(@CookieValue(value = "sessionID", required = false) String sessionIDCookie, HttpServletResponse res, @RequestParam String username, @RequestParam String email, @RequestParam String password) {
        try {
            deleteSession(sessionIDCookie, res);
            Connection conn = getConnection();
            ResultSet rs = conn.prepareStatement("SELECT COUNT(*) AS total FROM marketplace.users").executeQuery();
            Cookie cookie = createSessionCookie(res);

            if (rs.next()) {
                String query = String.format("INSERT INTO users VALUES (%d, '%s', '%s', '%s', '%s', '%s', '%s')", rs.getInt("total") + 1, username, codec.getEncoder().encode(password), email, "N/A", "2020-04-07", cookie.getValue());
                conn.prepareStatement(query).execute();
            }

            conn.close();
            return "redirect:";
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Connection getConnection() {
        try {
            return DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/marketplace", "webdev", "ai_marketplace");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Cookie createSessionCookie(HttpServletResponse res) {
        Cookie cookie = new Cookie("sessionID", UUID.randomUUID().toString());
        res.addCookie(cookie);
        return cookie;
    }

    @GetMapping("isUsernameAvailable")
    @ResponseBody
    public Boolean isUsernameAvailable(@RequestParam String username) {
        if (username.equals("admin")) {
            return false;
        } else {
            try {
                Connection conn = getConnection();
                ResultSet rs = conn.prepareStatement("SELECT * FROM marketplace.users").executeQuery();

                while (rs.next()) {
                    if (rs.getString("username").equals(username)) {
                        return false;
                    }
                }

                conn.close();
                return true;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @GetMapping("deleteSession")
    @ResponseBody
    public void deleteSession(@CookieValue(required = false) String sessionID, HttpServletResponse res) {
        if (sessionID != null) {
            try {
                Connection conn = getConnection();
                String query = String.format("UPDATE marketplace.users SET sessionID = NULL WHERE sessionID = '%s'", sessionID);
                conn.prepareStatement(query).execute();

                Cookie cookie = new Cookie("sessionID", "ignored");
                cookie.setMaxAge(0);
                res.addCookie(cookie);
                conn.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @GetMapping("id")
    @ResponseBody
    public int idOfSession(@CookieValue(required = false, defaultValue = "-1") String sessionID) {
        if (sessionID.equals("-1")) {
            return -1;
        }
        else {
            try {
                Connection conn = getConnection();
                String query = String.format("SELECT uid FROM marketplace.users WHERE sessionID='%s'", sessionID);
                ResultSet rs = conn.prepareStatement(query).executeQuery();
                String uid = "-1";
                while (rs.next()) {
                    uid = rs.getString("uid");
                }
                return Integer.parseInt(uid);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}