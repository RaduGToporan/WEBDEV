package com.green.marketplace;

import com.green.marketplace.user.Codec;
import com.green.marketplace.user.User;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.sql.*;
import java.util.UUID;

import javax.management.RuntimeOperationsException;

import java.util.List;
import java.util.ArrayList;
import java.text.SimpleDateFormat;

import com.green.marketplace.order.PastOrder;
import com.green.marketplace.order.OrderItem;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

@Controller
public class UserController {
    @Autowired
    private Codec codec;
    private final String sessionID = "sessionID";
    ObjectMapper objectMapper = new ObjectMapper(); // Utility to read a JSON-formatted List

    public String profile(Model model, String sessionId) {
        model.addAttribute("page", "Profile");
        int uid = idOfSession(sessionId);
        List<PastOrder> pastOrders = new ArrayList<>();


        try {
            Connection conn = getConnection();
            // Read user's orders
            ResultSet rs = conn.prepareStatement("SELECT orderid, username, time, address, status, products FROM marketplace.orders INNER JOIN marketplace.users ON orders.uid = users.uid WHERE orders.uid = "  + uid + ";").executeQuery();

            // Package as List
            while (rs.next()) {
                PastOrder newOrder = new PastOrder();
                newOrder.setOrderNum(rs.getInt("orderid"));
                newOrder.setUser(rs.getString("username"));

                SimpleDateFormat mmddyyyy = new SimpleDateFormat("dd/MM/yyyy");
                newOrder.setDate(mmddyyyy.format(rs.getDate("time")));

                newOrder.setTo(rs.getString("address"));
                newOrder.setStatus(rs.getString("status"));

                List<OrderItem> items = objectMapper.readValue(rs.getString("products"), new TypeReference<List<OrderItem>>(){});
                newOrder.setItems(items);

                pastOrders.add(newOrder);
            }

            model.addAttribute("pastOrders", pastOrders);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch(IOException e) { // For ObjectMapper
            e.printStackTrace();
        }

        return "user/profile";
    }

    public String dashboard(Model model) {
        model.addAttribute("page", "Dashboard");
        List<PastOrder> pastOrders = new ArrayList<>();

        try {
            Connection conn = getConnection();
            // Read user's orders
            ResultSet rs = conn.prepareStatement("SELECT orderid, username, time, address, status, products FROM marketplace.orders INNER JOIN marketplace.users ON orders.uid = users.uid;").executeQuery();

            // Package as List
            while (rs.next()) {
                PastOrder newOrder = new PastOrder();
                newOrder.setOrderNum(rs.getInt("orderid"));
                newOrder.setUser(rs.getString("username"));

                SimpleDateFormat mmddyyyy = new SimpleDateFormat("dd/MM/yyyy");
                newOrder.setDate(mmddyyyy.format(rs.getDate("time")));

                newOrder.setTo(rs.getString("address"));
                newOrder.setStatus(rs.getString("status"));

                List<OrderItem> items = objectMapper.readValue(rs.getString("products"), new TypeReference<List<OrderItem>>(){});
                newOrder.setItems(items);

                pastOrders.add(newOrder);
            }

            model.addAttribute("pastOrders", pastOrders);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch(IOException e) { // For ObjectMapper
            e.printStackTrace();
        }

        return "user/dashboard";
    }

    @GetMapping("login")
    public String login(@CookieValue(value = "sessionID", required = false) String sessionIDCookie, Model model) {
        if (sessionIDCookie == null) {
            return "user/login";
        }
        else {
            try {
                Connection conn = getConnection();
                ResultSet rs = conn.prepareStatement("SELECT * FROM marketplace.users").executeQuery();

                while (rs.next()) {
                    String sessionID = rs.getString("sessionID");
                    if (sessionID != null && sessionID.equals(sessionIDCookie)) {
                        model.addAttribute("user", new User(rs.getString("username"), rs.getString("password"), rs.getString("email")));
                        if (rs.getString("username").equals("admin")) {
                            return dashboard(model);
                        }
                        else {
                            return profile(model, sessionID);
                        }
                    }
                }

                return profile(model, sessionID);
            }
            catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @PostMapping("login")
    public String login(@CookieValue(value = "sessionID", required = false) String sessionIDCookie, HttpServletResponse res, @RequestParam(defaultValue = "user") String username, @RequestParam String password, Model model) {
        try {
            Connection conn = getConnection();
            ResultSet rs = conn.prepareStatement("SELECT * FROM marketplace.users").executeQuery();

            while (rs.next()) {
                String rsName = rs.getString("username");
                String rsPass = rs.getString("password");
                if (username.equals(rsName) && codec.getEncoder().matches(password, rsPass)) {
                    deleteSession(sessionIDCookie, res);
                    Cookie cookie = createCookie(res, sessionID, "");
                    conn.prepareStatement(String.format("UPDATE marketplace.users SET sessionID='%s' WHERE username='%s' AND password='%s'", cookie.getValue(), rsName, rsPass)).execute();
                    model.addAttribute("user", new User(rsName, password, rs.getString("email")));
                    conn.close();
                    if (username.equals("admin")) {
                        return dashboard(model);
                    }
                    else {
                        return profile(model, cookie.getValue());
                    }
                }
            }

            createCookie(res, "valid_credentials", "false");
            conn.close();
            return "redirect:login";
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("signup")
    public String signup() {
        return "user/signup";
    }

    /* Unique ID is prone to error if we allow deletion of accounts but isn't a requirement, so it's fine */
    @PostMapping("signup")
    public void signupPost(@CookieValue(value = "sessionID", required = false) String sessionIDCookie, HttpServletResponse res, @RequestParam String username, @RequestParam String email, @RequestParam String password) {
        try {
            deleteSession(sessionIDCookie, res);
            Connection conn = getConnection();
            ResultSet rs = conn.prepareStatement("SELECT COUNT(*) AS total FROM marketplace.users").executeQuery();
            Cookie cookie = createCookie(res, sessionID, "");

            if (rs.next()) {
                String query = String.format("INSERT INTO users VALUES (%d, '%s', '%s', '%s', '%s', '%s', '%s')", rs.getInt("total") + 1, username, codec.getEncoder().encode(password), email, "N/A", "2020-04-07", cookie.getValue());
                conn.prepareStatement(query).execute();
            }

            conn.close();
            res.sendRedirect("/login");
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    /* Utility */
    public Connection getConnection() {
        try {
            return DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/marketplace", "webdev", "ai_marketplace");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Cookie createCookie(HttpServletResponse res, String key, String value) {
        Cookie cookie = new Cookie(key, value);

        if (key.equals(sessionID)) {
            cookie.setValue(UUID.randomUUID().toString());
        }

        res.addCookie(cookie);
        return cookie;
    }

    /* Client-side utility */
    @GetMapping("isUsernameAvailable")
    @ResponseBody
    public Boolean isUsernameAvailable(@RequestParam String username) {
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

    @GetMapping("deleteSession")
    @ResponseBody
    public void deleteSession(@CookieValue(value="sessionID", required = false) String sessionID, HttpServletResponse res) {
        if (sessionID != null) {
            try {
                Connection conn = getConnection();
                String query = String.format("UPDATE marketplace.users SET sessionID = NULL WHERE sessionID = '%s'", sessionID);
                conn.prepareStatement(query).execute();
                Cookie cookie = new Cookie("sessionID", "");
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

    public boolean isAdmin(String sessionID) {
        Connection conn = getConnection();
        int uid = idOfSession(sessionID);

        try {
            ResultSet rs = conn.prepareStatement("SELECT username FROM  marketplace.users WHERE users.uid = " + uid  + ";").executeQuery();
            if (rs.next()) {
                if (rs.getString("username").equals("admin")) {
                    return true;
                }
            }
        } catch(SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    public String getUsername(int uid) {
        return "Bob";
    }
}