package com.green.marketplace;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {

	@GetMapping("/")
	public String home(Model model) {
		model.addAttribute("page", "Home");
		return "index";
	}

	@GetMapping("/about")
	public String about(Model model) {
		model.addAttribute("page", "About");
		return "index";
	}

	@GetMapping("/contact")
	public String contact(Model model) {
		model.addAttribute("page", "Contact");
		return "index";
	}
}