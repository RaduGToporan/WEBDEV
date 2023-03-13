package com.green.marketplace;

import com.green.marketplace.service.ModelService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

@Controller
public class ModelBeanController {

    private ModelService modelService = new ModelService();

    @PostMapping("addmodel")
    public void addModel(HttpServletRequest request, HttpServletResponse response) {
        ModelBean modelItem = new ModelBean();
        Map<String, String[]> paramMap = request.getParameterMap();
        modelItem.setName(paramMap.get("name")[0]);
        modelItem.setTrainedPrice(Double.parseDouble(paramMap.get("trainedPrice")[0]));
        modelItem.setUntrainedPrice(Double.parseDouble(paramMap.get("untrainedPrice")[0]));
        modelItem.setTags(Arrays.asList(paramMap.get("tags")[0].split(",")));
        modelService.addModel(modelItem);
        try {
            response.sendRedirect("/login");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @PostMapping("savemodel")
    public void saveModel(HttpServletRequest request, HttpServletResponse response) {
        ModelBean modelItem = new ModelBean();
        Map<String, String[]> paramMap = request.getParameterMap();
        modelItem.setId(Integer.parseInt(paramMap.get("id")[0]));
        modelItem.setName(paramMap.get("name")[0]);
        modelItem.setTrainedPrice(Double.parseDouble(paramMap.get("trainedPrice")[0]));
        modelItem.setUntrainedPrice(Double.parseDouble(paramMap.get("untrainedPrice")[0]));
        modelItem.setTags(Arrays.asList(paramMap.get("tags")[0].split(",")));
        modelService.saveModel(modelItem);
        try {
            response.sendRedirect("/login");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
