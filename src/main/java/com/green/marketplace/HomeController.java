package com.green.marketplace;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

	@GetMapping("/")
	public String home(Model model) {
		model.addAttribute("page", "Home");
		return "index";
	}

	@GetMapping("/wireframe")
	public String wireframe() {
		return "wireframe";
	}

	@GetMapping("/contact")
	public String contact(Model model) {
		model.addAttribute("page", "Contact");
		return "contact";
	}

	@GetMapping("/report")
	public String about(Model model) {
		model.addAttribute("page", "Report");
		return "report";
	}

}