package com.nutrition.portal.controller;

import com.nutrition.portal.dto.BeneficiaryDTO;
import com.nutrition.portal.dto.FoodItemDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Controller
public class PortalViewController {

    @Autowired
    private RestTemplate restTemplate;

    private final String BACKEND_INVENTORY_URL = "http://localhost:8081/api/inventory";
    private final String BACKEND_BENEFICIARIES_URL = "http://localhost:8081/api/beneficiaries";

    // 1. Landing / Home page -> index.html
    @GetMapping("/")
    public String showIndex() {
        return "index";
    }

    // 2. Inventory listing -> inventory.html
    @GetMapping("/inventory")
    public String showInventory(Model model) {
        try {
            FoodItemDTO[] items = restTemplate.getForObject(BACKEND_INVENTORY_URL, FoodItemDTO[].class);
            List<FoodItemDTO> itemList = (items != null) ? Arrays.asList(items) : List.of();
            model.addAttribute("items", itemList);
        } catch (Exception e) {
            model.addAttribute("items", List.of());
            model.addAttribute("error", "Backend service (port 8081) is not reachable.");
        }
        return "inventory";
    }

    // 3. Procurement / Add Food Form -> form.html
    @GetMapping("/form")
    public String showAddFoodForm(Model model) {
        model.addAttribute("foodItem", new FoodItemDTO());
        return "form";
    }

    // Process Add Food Form submission
    @PostMapping("/save")
    public String saveFoodItem(@ModelAttribute("foodItem") FoodItemDTO foodItem) {
        try {
            restTemplate.postForEntity(BACKEND_INVENTORY_URL, foodItem, FoodItemDTO.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/inventory";
    }

    // 4. Beneficiaries List -> beneficiaries.html
    @GetMapping("/beneficiaries")
    public String showBeneficiaries(Model model) {
        try {
            BeneficiaryDTO[] beneficiaries = restTemplate.getForObject(BACKEND_BENEFICIARIES_URL, BeneficiaryDTO[].class);
            List<BeneficiaryDTO> beneficiaryList = (beneficiaries != null) ? Arrays.asList(beneficiaries) : List.of();
            model.addAttribute("beneficiaries", beneficiaryList);
        } catch (Exception e) {
            model.addAttribute("beneficiaries", List.of());
            model.addAttribute("error", "Backend service (port 8081) is not reachable.");
        }
        return "beneficiaries";
    }

    // Delete item endpoint
    @GetMapping("/delete/{id}")
    public String deleteFoodItem(@PathVariable("id") Long id) {
        try {
            restTemplate.delete(BACKEND_INVENTORY_URL + "/" + id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/inventory";
    }
}