package com.green.marketplace;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@Controller
public class ProductController {

	ArrayList<ModelBean> modelDB = readDB();
	HashMap<String,Integer> tagMap = new HashMap<>();
	int count = 0;
	


    @GetMapping("/catalogue")
	public String catalogue(Model model) {
		modelDB = readDB();
		model.addAttribute("page", "Catalogue");
		model.addAttribute("models", modelDB);
		model.addAttribute("tags", new ArrayList<String>(tagMap.keySet()));
		return "products/catalogue";
	}

	@PostMapping("/catalogue")
	public String cataloguePost(Model model, @RequestBody String data) {
		
		modelDB = readDB();

		System.out.println(data);

		model.addAttribute("page", "Catalogue");

		ArrayList<String> filterTags = new ArrayList<>();

		filterTags.add("plant");
		filterTags.add("other");
		filterTags.add("image");

		ArrayList<ModelBean> filtered = filterModels(modelDB, filterTags);


		model.addAttribute("models", filtered);





		model.addAttribute("tags", new ArrayList<String>(tagMap.keySet()));
		return "products/catalogue";
	}
    
    @GetMapping("/product/{id}")
	public String product(@PathVariable int id, Model model) {

		modelDB = readDB();
		model.addAttribute("page", "Product");
		model.addAttribute("id", id);
		int productID = -1;
		for(int i = 0; i<modelDB.size();i++){
			if(modelDB.get(i).getId() == id){
				productID = i;
				break;
			}
		}

		model.addAttribute("product", modelDB.get(productID));

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
					String unprocessedtags = resultSet.getString("tags");
    				ArrayList<String> tags = formatTags(unprocessedtags);
					boolean available = resultSet.getBoolean("available");

					list.add(new ModelBean(id,name,trainedPrice,untrainedPrice,unprocessedtags,tags,available));
					
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
		String[] res = s.split(",");

		for(int i = 0; i<res.length;i++){
			list.add(res[i]);
			tagMap.put(res[i], count%2);
			count++;
		}
		return list;
	}

	private ArrayList<ModelBean> filterModels(ArrayList<ModelBean> list, ArrayList<String> tags){
		if(tags.isEmpty()){
			return list;
		} else{
			ArrayList<ModelBean> filtered = new ArrayList<>();

			for(int i = 0; i<list.size();i++){
				for(int j =0;j<tags.size();j++){
					if(list.get(i).getTags().contains(tags.get(j)) && !filtered.contains(list.get(i))){
						filtered.add(list.get(i));
					}
				}
			}

			return filtered;
		}
	}
	
}
