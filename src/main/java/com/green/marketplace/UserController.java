package com.green.marketplace;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.*;
import java.util.UUID;

@Controller
public class UserController {
	@GetMapping("/profile")
	public String profile(HttpServletResponse res, @RequestParam(defaultValue="user") String username, @RequestParam String password, Model model) {
		if (username.equals("admin")) {
			if (password.equals("ai_marketplace")) {
				model.addAttribute("page", "Dashboard");
				return "user/dashboard";
			}
        } else if (!username.equals("user")){
			SCryptPasswordEncoder codec = new SCryptPasswordEncoder(64, 8, 1, 32, 16);
			Connection connection = connectDatabase();

			try {
				ResultSet rs = connection.prepareStatement("SELECT * FROM marketplace.users").executeQuery();
				while (rs.next()) {
					if (username.equals(rs.getString("username")) && codec.matches(password, rs.getString("password"))) {
						UUID sessionID = UUID.randomUUID();
						String query = String.format("UPDATE marketplace.users SET sessionID='%s' WHERE password='%s' AND username='%s'", sessionID, rs.getString("password"), rs.getString("username"));
						connection.prepareStatement(query).execute();
						Cookie cookie = new Cookie("sessionID", sessionID.toString());
						res.addCookie(cookie);
						model.addAttribute("page", "User");
						return "user/profile";
					}
				}
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
        }

		return "index";
	}

    @GetMapping("/login")
	public String login(Model model) {
		return "user/login";
	}
    

    @GetMapping("/signup")
	public String signup(Model model) {
		return "user/signup";
	}
	@PostMapping("/signup")
	public String signupPost(HttpServletResponse res, @RequestParam String username, @RequestParam String email, @RequestParam String password) {
		// Unique ID is prone to error if we allow deletion of accounts but isn't a requirement
		SCryptPasswordEncoder codec = new SCryptPasswordEncoder(64, 8, 1, 32, 16);
		Connection connection = connectDatabase();
		try {
			ResultSet rs = connection.prepareStatement("SELECT COUNT(*) AS total FROM marketplace.users").executeQuery();
			String query = "";
			UUID sessionID = UUID.randomUUID();
			if (rs.next()) {
				query = String.format("INSERT INTO users VALUES (%d, '%s', '%s', '%s', '%s', '%s', '%s')", rs.getInt("total"), username, codec.encode(password), email, "N/A", "2020-04-07", sessionID);
			}
			Cookie cookie = new Cookie("sessionID", sessionID.toString());
			res.addCookie(cookie);
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			preparedStatement.execute();
			connection.close();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return "index";
	}

	public boolean validSession(Boolean sessionID) {
		return false;
	}

	public Connection connectDatabase() {
		try {
			return DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/marketplace", "webdev", "ai_marketplace");
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

	}
}