package com.green.marketplace;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

}