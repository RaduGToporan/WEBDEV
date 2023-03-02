package com.green.marketplace;

import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

@Controller
public class UserController {

	@GetMapping("/profile")
	public String profile(@RequestParam(name="type", required=false, defaultValue="user") String name, Model model) {
        if (name.equals("admin")) {
            model.addAttribute("page", "Dashboard");
		    return "user/dashboard";
        } else {
            model.addAttribute("page", "Profile");
		    return "user/profile";
        }
		
	}

    @GetMapping("/login")
	public String login(Model model) {
		model.addAttribute("page", "Log In");
		return "user/login";
	}
    

    @GetMapping("/signup")
	public String home(Model model) {
		model.addAttribute("page", "Sign Up");
		return "user/signup";
	}
	@PostMapping("/signup")
	public String signupPost(@RequestParam String username, @RequestParam String email, @RequestParam String password) {
		// TODO create incrementing uniqueID
		SCryptPasswordEncoder codec = new SCryptPasswordEncoder(64, 8, 1, 32, 16);
		Connection connection = database();
		String query = String.format("INSERT INTO users VALUES (%d, '%s', '%s', '%s', '%s', '%s', '%s')", 72, username, codec.encode(password), email, "N/A", "2020-04-07", UUID.randomUUID());
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			preparedStatement.execute();
			connection.close();
			System.out.println("ES");
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return "index";
	}

	public Connection database() {
		try {
			return DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/marketplace", "webdev", "ai_marketplace");
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

	}
}