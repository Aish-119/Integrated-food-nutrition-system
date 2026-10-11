package com.nutrition.portal.controller;

import com.nutrition.portal.dto.BeneficiaryDTO;
import com.nutrition.portal.dto.InventoryBatchDTO;

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

    private static final String INVENTORY_BATCHES_URL = "http://localhost:8081/api/inventory-batches";

    private static final String FOOD_ITEMS_URL = "http://localhost:8081/api/food-items";

    private static final String BENEFICIARIES_URL = "http://localhost:8082/api/beneficiaries";

    @GetMapping("/")
    public String showIndex() {
        return "index";
    }

    // Display inventory batches
    @GetMapping("/inventory")
    public String showInventory(Model model) {
        try {
            InventoryBatchDTO[] batches = restTemplate.getForObject(
                    INVENTORY_BATCHES_URL,
                    InventoryBatchDTO[].class);

            List<InventoryBatchDTO> batchList = batches != null ? Arrays.asList(batches) : List.of();

            model.addAttribute("items", batchList);

        } catch (Exception e) {
            model.addAttribute("items", List.of());
            model.addAttribute(
                    "error",
                    "Cannot connect to Food Backend on port 8081.");
        }

        return "inventory";
    }

    // Display the add-batch form
    @GetMapping("/form")
    public String showAddFoodForm(Model model) {
        model.addAttribute("foodItem", new InventoryBatchDTO());
        return "form";
    }

    // Save a new inventory batch
    @PostMapping("/save")
    public String saveFoodItem(
            @ModelAttribute("foodItem") InventoryBatchDTO batch,
            Model model) {
        try {
            restTemplate.postForEntity(
                    INVENTORY_BATCHES_URL,
                    batch,
                    InventoryBatchDTO.class);

            return "redirect:/inventory";

        } catch (Exception e) {
            model.addAttribute("error",
                    "Could not save the batch. Check the food item ID and backend.");
            return "form";
        }
    }

    // Delete an inventory batch
    @GetMapping("/delete/{id}")
    public String deleteFoodItem(@PathVariable Long id) {
        try {
            restTemplate.delete(INVENTORY_BATCHES_URL + "/" + id);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "redirect:/inventory";
    }

    // Display beneficiaries from Nutrition Service
    @GetMapping("/beneficiaries")
    public String showBeneficiaries(Model model) {
        try {
            BeneficiaryDTO[] beneficiaries = restTemplate.getForObject(
                    BENEFICIARIES_URL,
                    BeneficiaryDTO[].class);

            List<BeneficiaryDTO> beneficiaryList = beneficiaries != null
                    ? Arrays.asList(beneficiaries)
                    : List.of();

            model.addAttribute("beneficiaries", beneficiaryList);

        } catch (Exception e) {
            model.addAttribute("beneficiaries", List.of());
            model.addAttribute(
                    "error",
                    "Cannot connect to Nutrition Service on port 8082.");
        }

        return "beneficiaries";
    }

    // Keep the additional frontend pages
    @GetMapping("/procurement")
    public String showProcurement() {
        return "procurement";
    }

    @GetMapping("/programmes")
    public String showProgrammes() {
        return "programmes";
    }

    @GetMapping("/distribution")
    public String showDistribution() {
        return "distribution";
    }

    @GetMapping("/beneficiaries/add")
    public String showAddBeneficiaryForm(Model model) {
        model.addAttribute("beneficiary", new BeneficiaryDTO());
        return "beneficiary-form";
    }

    @PostMapping("/beneficiaries/save")
    public String saveBeneficiary(
            @ModelAttribute("beneficiary") BeneficiaryDTO beneficiary,
            Model model) {
        try {
            restTemplate.postForEntity(
                    BENEFICIARIES_URL,
                    beneficiary,
                    BeneficiaryDTO.class);
            return "redirect:/beneficiaries";
        } catch (Exception e) {
            model.addAttribute("error",
                    "Could not save beneficiary. Check the details and Nutrition Service.");
            return "beneficiary-form";
        }
    }
}