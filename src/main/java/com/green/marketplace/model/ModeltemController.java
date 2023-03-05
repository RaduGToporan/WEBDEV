package com.green.marketplace.model;

import com.green.marketplace.service.ModelService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Controller
public class ModeltemController {

    private ModelService modelService = new ModelService();

    @GetMapping("/models")
    public String home(Model model) {
        List<ModelItem> modelList = modelService.getAllModels();
        model.addAttribute("modelList", modelList);
        return "models";
    }

    @PostMapping("addmodel")
    public void addModel(HttpServletRequest request, HttpServletResponse response) {
        ModelItem modelItem = new ModelItem();
        Map<String, String[]> paramMap = request.getParameterMap();
        modelItem.setName(paramMap.get("name")[0]);
        modelItem.setTrainedPrice(Double.parseDouble(paramMap.get("trainedPrice")[0]));
        modelItem.setUntrainedPrice(Double.parseDouble(paramMap.get("untrainedPrice")[0]));
        modelItem.setTags(paramMap.get("tags")[0]);
        modelService.addModel(modelItem);
        try {
            response.sendRedirect("/models");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @PostMapping("savemodel")
    public void saveModel(HttpServletRequest request, HttpServletResponse response) {
        ModelItem modelItem = new ModelItem();
        Map<String, String[]> paramMap = request.getParameterMap();
        modelItem.setModelId(Long.parseLong(paramMap.get("id")[0]));
        modelItem.setName(paramMap.get("name")[0]);
        modelItem.setTrainedPrice(Double.parseDouble(paramMap.get("trainedPrice")[0]));
        modelItem.setUntrainedPrice(Double.parseDouble(paramMap.get("untrainedPrice")[0]));
        modelItem.setTags(paramMap.get("tags")[0]);
        modelService.saveModel(modelItem);
        try {
            response.sendRedirect("/models");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}