package com.green.marketplace;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class OrderController {

	@GetMapping("/order/cart")
	public String cart(Model model) {
		model.addAttribute("page", "Shopping Cart");
		return "order/cart";
	}

	@GetMapping("/order/checkout")
	public String checkout(Model model) {
		model.addAttribute("page", "Checkout");
		return "order/checkout";
	}

}