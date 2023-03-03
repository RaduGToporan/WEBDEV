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
	@GetMapping("login")
	public String login() {
		return "user/login";
	}

	@PostMapping("profile")
	public String profile(HttpServletResponse res, @RequestParam(defaultValue="user") String username, @RequestParam String password, Model model) {
		if (username.equals("admin") && password.equals("ai_marketplace")) {
			model.addAttribute("page", "Dashboard");
			return "user/dashboard";
        } else if (!username.equals("user")){
			SCryptPasswordEncoder codec = new SCryptPasswordEncoder(64, 8, 1, 32, 16);

			try {
				Connection conn = connectDatabase();
				ResultSet rs = conn.prepareStatement("SELECT * FROM marketplace.users").executeQuery();
				String rsName = rs.getString("username");
				String rsPass = rs.getString("password");

				while (rs.next()) {
					if (username.equals(rsName) && codec.matches(password, rs.getString("rsPass"))) {
						UUID sessionID = UUID.randomUUID();
						String query = String.format("UPDATE marketplace.users SET sessionID='%s' WHERE username='%s' AND password='%s'", sessionID, rsName, rsPass);

						conn.prepareStatement(query).execute();
						res.addCookie(new Cookie("sessionID", sessionID.toString()));
						model.addAttribute("page", "User");
						return "redirect:/user/profile";
					}
				}

				conn.close();
			} catch (SQLException e) {
				model.addAttribute("Invalid login");
				return "redirect:/";
			}
        }

		model.addAttribute("Invalid login");
		return "redirect:/";
	}

    @GetMapping("signup")
	public String signup() {
		return "user/signup";
	}

	/*
	* Unique ID is prone to error if we allow deletion of accounts but isn't a requirement, so it's fine
	* */
	@PostMapping("signup")
	public String signupPost(HttpServletResponse res, @RequestParam String username, @RequestParam String email, @RequestParam String password) {
		try {
			SCryptPasswordEncoder codec = new SCryptPasswordEncoder(64, 8, 1, 32, 16);
			Connection conn = connectDatabase();
			ResultSet rs = conn.prepareStatement("SELECT COUNT(*) AS total FROM marketplace.users").executeQuery();
			String query = "";
			UUID sessionID = UUID.randomUUID();

			if (rs.next()) {
				query = String.format("INSERT INTO users VALUES (%d, '%s', '%s', '%s', '%s', '%s', '%s')", rs.getInt("total"), username, codec.encode(password), email, "N/A", "2020-04-07", sessionID);
			}

			res.addCookie(new Cookie("sessionID", sessionID.toString()));
			conn.prepareStatement(query);
			conn.close();
			return "redirect:/";
		} catch (SQLException e) {
			return "redirect:/";
		}
	}

	public Connection connectDatabase() {
		try {
			return DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/marketplace", "webdev", "ai_marketplace");
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}