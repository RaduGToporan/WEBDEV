package com.green.marketplace;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@Controller
public class ProductController {

	ArrayList<ModelBean> modelDB = readDB();


    @GetMapping("/catalogue")
	public String catalogue(Model model) {
		modelDB = readDB();
		model.addAttribute("page", "Catalogue");
		model.addAttribute("models", modelDB);
		return "products/catalogue";
	}
    
    @GetMapping("/product")
	public String product(@RequestParam(name="id", required=false, defaultValue="0") String name, Model model) {

        var id = Integer.parseInt(name);
		model.addAttribute("page", "Product");
		model.addAttribute("id", id);

		return "products/product";
	}

	private ArrayList<ModelBean>  readDB(){
		String SQL_SELECT = "Select * from models";
		ArrayList<ModelBean> list = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(
                "jdbc:mysql://127.0.0.1:3306/marketplace", "webdev", "ai_marketplace")) {

            if (conn != null) {
                System.out.println("Connected to the database!");
                PreparedStatement preparedStatement = conn.prepareStatement(SQL_SELECT);

                ResultSet resultSet = preparedStatement.executeQuery();

                while(resultSet.next()){
					int id = resultSet.getInt("modelid");
    				String name = resultSet.getString("name");
    				int trainedPrice = resultSet.getInt("trainedprice");
    				int untrainedPrice = resultSet.getInt("untrainedprice");
    				ArrayList<String> tags = formatTags(resultSet.getString("tags"));
					boolean available = resultSet.getBoolean("available");

					list.add(new ModelBean(id,name,trainedPrice,untrainedPrice,tags,available));
					
                    
                }
			return list;	
            } else {
                return null;
            }

        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
		return null;
    }


	private ArrayList<String> formatTags(String s){
		ArrayList<String> list = new ArrayList<>();


		return list;
	}
	
}
