package com.nutrition.portal.controller;

import com.nutrition.portal.dto.FoodItemDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Controller
public class PortalViewController {

    private final String BACKEND_URL = "http://localhost:8081/api/inventory";
    private final RestTemplate restTemplate = new RestTemplate();

    @GetMapping("/")
    public String viewDashboard(Model model) {
        try {
            FoodItemDTO[] items = restTemplate.getForObject(BACKEND_URL, FoodItemDTO[].class);
            List<FoodItemDTO> itemList = (items != null) ? Arrays.asList(items) : List.of();
            model.addAttribute("items", itemList);
        } catch (Exception e) {
            model.addAttribute("items", List.of());
            model.addAttribute("error", "Backend service (port 8081) is not reachable yet.");
        }
        model.addAttribute("newItem", new FoodItemDTO());
        return "dashboard";
    }

    @PostMapping("/procure")
    public String procureFoodItem(@ModelAttribute("newItem") FoodItemDTO item) {
        try {
            restTemplate.postForEntity(BACKEND_URL, item, FoodItemDTO.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/";
    }

    @GetMapping("/delete/{id}")
    public String removeFoodItem(@PathVariable Long id) {
        try {
            restTemplate.delete(BACKEND_URL + "/" + id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/";
    }
}
