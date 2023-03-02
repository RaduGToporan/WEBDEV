package com.green.marketplace.order;

import java.io.IOException;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

@Controller
public class OrderController {

	// Go to an empty shopping cart page so that JS can POST shopping cart data back
	@GetMapping("/order/cart")
	public String cart(Model model) {
		model.addAttribute("page", "Shopping Cart");
		model.addAttribute("getRequest", true); // This is a GET request
		return "order/cart";
	}

	// JS has POSTed shopping cart data
	@PostMapping("/order/cart")
	public String cart(@RequestParam String scJson, @RequestParam int cartCount, Model model) {
		// scJson is the shopping cart Map formatted as JSON
		// cartCount is the total quantity of items in the cart

		// Connect to the SQL database
		try (Connection conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/marketplace", "webdev", "ai_marketplace")) {
			ObjectMapper objectMapper = new ObjectMapper(); // Utility to read a JSON-formatted Map

			model.addAttribute("page", "Shopping Cart");
			model.addAttribute("getRequest", false); //This is not a GET request
			model.addAttribute("cartEmpty", cartCount == 0);

			try{ // try block required for ObjectMapper
				Map<String, Integer> scMap = objectMapper.readValue(scJson, Map.class); // Parse the JSON-formatted Map
				ArrayList<CartItem> scList = new ArrayList<>(); // List of items in the cart
				ArrayList<String> unavailableItems = new ArrayList<>(); // List of items in the cart that are not available

				for (String key : scMap.keySet()) {
					// The last character in the key string determines whether the model is trained or not
					String productId = key.substring(0, key.length()-1);
					boolean trained = (key.substring(key.length()-1).equals("t"));

					// Make the SQL query for the model
					PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM models WHERE models.modelid=" + productId);
                	ResultSet resultSet = preparedStatement.executeQuery();

					// With the result from the SQL query
					if (resultSet.next()) { 
						String itemName = resultSet.getString("name");
						int unitPrice;
						int quantity = scMap.get(key);
						boolean available = resultSet.getBoolean("available");

						if (available && quantity > 0) {
							// Make appropriate changes depending on if model is trained
							if (trained) {
								itemName += " (Trained)";
								unitPrice = resultSet.getInt("trainedprice");
							} else {
								itemName += " (Untrained)";
								unitPrice = resultSet.getInt("untrainedprice");
							}

							scList.add(new CartItem(key, itemName, unitPrice/100.0, quantity));
						} else if (!available && quantity > 0) {
							if (!unavailableItems.contains(itemName)) { // Don't add both trained and untrained models
								unavailableItems.add(itemName);
							}

						}
					}
				}
				model.addAttribute("items", scList);
				if (unavailableItems.size() > 0) {
					model.addAttribute("unavailable", unavailableItems);
				}
				
			} catch(IOException e) { // For ObjectMapper
				e.printStackTrace();
			}
		} catch (SQLException e) { // For SQL Connection
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
		
		return "/order/cart";
	}

	@GetMapping("/order/checkout")
	public String checkout(Model model) {
		model.addAttribute("page", "Checkout");
		return "order/checkout";
	}

	@PostMapping("/order/checkout")
	public String checkout(@RequestParam String checkoutItems, Model model) {
		ObjectMapper objectMapper = new ObjectMapper();
		
		try {
			List<CheckoutItem> x = objectMapper.readValue(checkoutItems, new TypeReference<List<CheckoutItem>>(){});
			model.addAttribute("items", x);
		} catch (IOException e) {
			e.printStackTrace();
		}
		model.addAttribute("page", "Checkout");
		return "order/checkout";
	}

}