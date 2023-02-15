package com.green.marketplace;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ProductController {
    @GetMapping("/catalogue")
	public String catalogue(Model model) {
		model.addAttribute("page", "Catalogue");
		return "products/catalogue";
	}
    
    @GetMapping("/product")
	public String product(@RequestParam(name="id", required=false, defaultValue="0") String name, Model model) {
        var id = Integer.parseInt(name);
		model.addAttribute("page", "Product");
		model.addAttribute("id", id);
		return "products/product";
	}
}
