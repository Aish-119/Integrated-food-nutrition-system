package com.nutrition.portal.controller;

import com.nutrition.portal.dto.BeneficiaryDTO;
import com.nutrition.portal.dto.InventoryBatchDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class PortalViewController {

    @Autowired
    private RestTemplate restTemplate;

    private static final String INVENTORY_BATCHES_URL = "http://localhost:8081/api/inventory-batches";

    private static final String FOOD_ITEMS_URL = "http://localhost:8081/api/food-items";

    private static final String BENEFICIARIES_URL = "http://localhost:8082/api/beneficiaries";

    private static final String DISTRIBUTIONS_URL = "http://localhost:8082/api/distributions";

    private static final String PROCUREMENT_URL = "http://localhost:8081/api/procurements";

    private static final String QUALITY_URL = "http://localhost:8081/api/quality-inspections";

    private static final String WASTAGE_URL = "http://localhost:8081/api/wastage-records";

    private static final String PROGRAMMES_URL = "http://localhost:8082/api/programmes";

    // HOME

    @GetMapping("/")
    public String showIndex() {
        return "index";
    }

    // INVENTORY

    @GetMapping("/inventory")
    public String showInventory(Model model) {
        try {
            InventoryBatchDTO[] batches = restTemplate.getForObject(
                    INVENTORY_BATCHES_URL,
                    InventoryBatchDTO[].class);

            model.addAttribute("items",
                    batches != null ? Arrays.asList(batches) : List.of());

        } catch (Exception e) {
            model.addAttribute("items", List.of());
            model.addAttribute("error",
                    "Cannot connect to Food Backend on port 8081.");
        }

        return "inventory";
    }

    @GetMapping("/form")
    public String showAddFoodForm(Model model) {
        model.addAttribute("foodItem", new InventoryBatchDTO());
        return "form";
    }

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

    @GetMapping("/delete/{id}")
    public String deleteFoodItem(@PathVariable Long id) {
        try {
            restTemplate.delete(INVENTORY_BATCHES_URL + "/" + id);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "redirect:/inventory";
    }

    // BENEFICIARIES

    @GetMapping("/beneficiaries")
    public String showBeneficiaries(Model model) {
        try {
            BeneficiaryDTO[] beneficiaries = restTemplate.getForObject(
                    BENEFICIARIES_URL,
                    BeneficiaryDTO[].class);

            model.addAttribute("beneficiaries",
                    beneficiaries != null
                            ? Arrays.asList(beneficiaries)
                            : List.of());

        } catch (Exception e) {
            model.addAttribute("beneficiaries", List.of());
            model.addAttribute("error",
                    "Cannot connect to Nutrition Service on port 8082.");
        }

        return "beneficiaries";
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

    // DISTRIBUTIONS

    @GetMapping("/distribution")
    public String showDistribution(
            @RequestParam(required = false) String success,
            @RequestParam(required = false) String updated,
            @RequestParam(required = false) String statusError,
            Model model) {

        loadDistributionData(model);
        model.addAttribute("today", LocalDate.now().toString());

        if (success != null) {
            model.addAttribute("success",
                    "Distribution created successfully.");
        }

        if (updated != null) {
            model.addAttribute("success",
                    "Distribution status updated successfully.");
        }

        if (statusError != null) {
            model.addAttribute("error",
                    "Could not update distribution status.");
        }

        return "distribution";
    }

    private void loadDistributionData(Model model) {

        try {
            BeneficiaryDTO[] beneficiaries = restTemplate.getForObject(
                    BENEFICIARIES_URL,
                    BeneficiaryDTO[].class);

            model.addAttribute("beneficiaries",
                    beneficiaries != null
                            ? Arrays.asList(beneficiaries)
                            : List.of());

        } catch (Exception e) {
            model.addAttribute("beneficiaries", List.of());
            model.addAttribute("error",
                    "Could not load beneficiaries from Nutrition Service.");
        }

        try {
            Map[] foodItems = restTemplate.getForObject(
                    FOOD_ITEMS_URL,
                    Map[].class);

            model.addAttribute("foodItems",
                    foodItems != null
                            ? Arrays.asList(foodItems)
                            : List.of());

        } catch (Exception e) {
            model.addAttribute("foodItems", List.of());
            model.addAttribute("error",
                    "Could not load food items from Food Backend.");
        }

        try {
            Map[] distributions = restTemplate.getForObject(
                    DISTRIBUTIONS_URL,
                    Map[].class);

            model.addAttribute("distributions",
                    distributions != null
                            ? Arrays.asList(distributions)
                            : List.of());

        } catch (Exception e) {
            model.addAttribute("distributions", List.of());
        }
    }

    @PostMapping("/distribution/create")
    public String createDistribution(
            @RequestParam Long beneficiaryId,
            @RequestParam Long foodItemId,
            @RequestParam BigDecimal quantity,
            @RequestParam String unit,
            @RequestParam String distributionDate,
            Model model) {

        try {
            Map<String, Object> item = new HashMap<>();
            item.put("foodItemId", foodItemId);
            item.put("quantity", quantity);
            item.put("unit", unit);

            Map<String, Object> request = new HashMap<>();
            request.put("beneficiaryId", beneficiaryId);
            request.put("distributionDate", distributionDate);
            request.put("items", List.of(item));

            restTemplate.postForEntity(
                    DISTRIBUTIONS_URL,
                    request,
                    Object.class);

            return "redirect:/distribution?success=true";

        } catch (Exception e) {
            loadDistributionData(model);
            model.addAttribute("today", LocalDate.now().toString());
            model.addAttribute("error",
                    "Distribution failed. Check stock, quantity, unit, and backend status.");

            return "distribution";
        }
    }

    @PostMapping("/distribution/{id}/status")
    public String updateDistributionStatus(
            @PathVariable Long id,
            @RequestParam String status) {

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, String>> request = new HttpEntity<>(Map.of("status", status), headers);

            restTemplate.exchange(
                    DISTRIBUTIONS_URL + "/" + id + "/status",
                    HttpMethod.PATCH,
                    request,
                    Object.class);

            return "redirect:/distribution?updated=true";

        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/distribution?statusError=true";
        }
    }

    // PROCUREMENT

    @GetMapping("/procurement")
    public String showProcurement(
            @RequestParam(required = false) String success,
            Model model) {

        loadProcurementData(model);
        model.addAttribute("today", LocalDate.now().toString());

        if (success != null) {
            model.addAttribute("success",
                    "Procurement recorded successfully.");
        }

        return "procurement";
    }

    private void loadProcurementData(Model model) {

        try {
            Map[] foodItems = restTemplate.getForObject(
                    FOOD_ITEMS_URL,
                    Map[].class);

            model.addAttribute("foodItems",
                    foodItems != null
                            ? Arrays.asList(foodItems)
                            : List.of());

        } catch (Exception e) {
            model.addAttribute("foodItems", List.of());
            model.addAttribute("error", "Could not load food items.");
        }

        try {
            Map[] procurements = restTemplate.getForObject(
                    PROCUREMENT_URL,
                    Map[].class);

            model.addAttribute("procurements",
                    procurements != null
                            ? Arrays.asList(procurements)
                            : List.of());

        } catch (Exception e) {
            model.addAttribute("procurements", List.of());
            model.addAttribute("error",
                    "Could not load procurement records.");
        }
    }

    @PostMapping("/procurement/create")
    public String createProcurement(
            @RequestParam Long foodItemId,
            @RequestParam BigDecimal quantity,
            @RequestParam String procurementDate,
            @RequestParam String supplier,
            Model model) {

        try {
            Map<String, Object> request = new HashMap<>();
            request.put("foodItemId", foodItemId);
            request.put("quantity", quantity);
            request.put("procurementDate", procurementDate);
            request.put("supplier", supplier);

            restTemplate.postForEntity(
                    PROCUREMENT_URL,
                    request,
                    Object.class);

            return "redirect:/procurement?success=true";

        } catch (Exception e) {
            loadProcurementData(model);
            model.addAttribute("today", LocalDate.now().toString());
            model.addAttribute("error",
                    "Could not save procurement. Check the food item and backend.");

            return "procurement";
        }
    }

    // QUALITY AND WASTAGE

    @GetMapping("/quality-wastage")
    public String showQualityWastage(
            @RequestParam(required = false) String success,
            Model model) {

        loadQualityWastageData(model);
        model.addAttribute("today", LocalDate.now().toString());

        if ("inspection".equals(success)) {
            model.addAttribute("success",
                    "Quality inspection recorded.");
        } else if ("wastage".equals(success)) {
            model.addAttribute("success",
                    "Wastage record saved.");
        }

        return "quality-wastage";
    }

    private void loadQualityWastageData(Model model) {

        try {
            Map[] foodItems = restTemplate.getForObject(
                    FOOD_ITEMS_URL,
                    Map[].class);

            model.addAttribute("foodItems",
                    foodItems != null
                            ? Arrays.asList(foodItems)
                            : List.of());

        } catch (Exception e) {
            model.addAttribute("foodItems", List.of());
            model.addAttribute("error", "Could not load food items.");
        }

        try {
            Map[] batches = restTemplate.getForObject(
                    INVENTORY_BATCHES_URL,
                    Map[].class);

            model.addAttribute("batches",
                    batches != null
                            ? Arrays.asList(batches)
                            : List.of());

        } catch (Exception e) {
            model.addAttribute("batches", List.of());
            model.addAttribute("error",
                    "Could not load inventory batches.");
        }

        try {
            Map[] inspections = restTemplate.getForObject(
                    QUALITY_URL,
                    Map[].class);

            model.addAttribute("inspections",
                    inspections != null
                            ? Arrays.asList(inspections)
                            : List.of());

        } catch (Exception e) {
            model.addAttribute("inspections", List.of());
            model.addAttribute("error",
                    "Could not load quality inspection records.");
        }

        try {
            Map[] wastage = restTemplate.getForObject(
                    WASTAGE_URL,
                    Map[].class);

            model.addAttribute("wastageRecords",
                    wastage != null
                            ? Arrays.asList(wastage)
                            : List.of());

        } catch (Exception e) {
            model.addAttribute("wastageRecords", List.of());
            model.addAttribute("error",
                    "Could not load wastage records.");
        }
    }

    @PostMapping("/quality-inspections/create")
    public String createQualityInspection(
            @RequestParam Long foodItemId,
            @RequestParam(required = false) Long batchId,
            @RequestParam String inspectionDate,
            @RequestParam String inspectorName,
            @RequestParam String qualityStatus,
            @RequestParam(required = false) String remarks,
            Model model) {

        try {
            Map<String, Object> request = new HashMap<>();
            request.put("foodItemId", foodItemId);
            request.put("batchId", batchId);
            request.put("inspectionDate", inspectionDate);
            request.put("inspectorName", inspectorName);
            request.put("qualityStatus", qualityStatus);
            request.put("remarks", remarks);

            restTemplate.postForEntity(
                    QUALITY_URL,
                    request,
                    Object.class);

            return "redirect:/quality-wastage?success=inspection";

        } catch (Exception e) {
            loadQualityWastageData(model);
            model.addAttribute("today", LocalDate.now().toString());
            model.addAttribute("error",
                    "Could not save quality inspection. Check the food item and batch.");

            return "quality-wastage";
        }
    }

    @PostMapping("/wastage-records/create")
    public String createWastageRecord(
            @RequestParam Long foodItemId,
            @RequestParam(required = false) Long batchId,
            @RequestParam BigDecimal quantity,
            @RequestParam String unit,
            @RequestParam String wastageReason,
            @RequestParam String wastageDate,
            @RequestParam(required = false) String remarks,
            Model model) {

        try {
            Map<String, Object> request = new HashMap<>();
            request.put("foodItemId", foodItemId);
            request.put("batchId", batchId);
            request.put("quantity", quantity);
            request.put("unit", unit);
            request.put("wastageReason", wastageReason);
            request.put("wastageDate", wastageDate);
            request.put("remarks", remarks);

            restTemplate.postForEntity(
                    WASTAGE_URL,
                    request,
                    Object.class);

            return "redirect:/quality-wastage?success=wastage";

        } catch (Exception e) {
            loadQualityWastageData(model);
            model.addAttribute("today", LocalDate.now().toString());
            model.addAttribute("error",
                    "Could not save wastage. Check quantity, item, batch, and backend.");

            return "quality-wastage";
        }
    }

    // NUTRITION PROGRAMMES

    @GetMapping("/programmes")
    public String showProgrammes(
            @RequestParam(required = false) String success,
            Model model) {

        loadProgrammes(model);
        model.addAttribute("today", LocalDate.now().toString());

        if (success != null) {
            model.addAttribute("success",
                    "Programme saved successfully.");
        }

        return "programmes";
    }

    private void loadProgrammes(Model model) {

        try {
            Map[] programmes = restTemplate.getForObject(
                    PROGRAMMES_URL,
                    Map[].class);

            model.addAttribute("programmes",
                    programmes != null
                            ? Arrays.asList(programmes)
                            : List.of());

        } catch (Exception e) {
            model.addAttribute("programmes", List.of());
            model.addAttribute("error",
                    "Could not load nutrition programmes.");
        }
    }

    @PostMapping("/programmes/create")
    public String createProgramme(
            @RequestParam String name,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam String status,
            Model model) {

        try {
            Map<String, Object> request = new HashMap<>();
            request.put("name", name);
            request.put("description", description);
            request.put("startDate",
                    startDate == null || startDate.isBlank()
                            ? null
                            : LocalDate.parse(startDate));
            request.put("endDate",
                    endDate == null || endDate.isBlank()
                            ? null
                            : LocalDate.parse(endDate));
            request.put("status", status);

            restTemplate.postForEntity(
                    PROGRAMMES_URL,
                    request,
                    Object.class);

            return "redirect:/programmes?success=true";

        } catch (Exception e) {
            loadProgrammes(model);
            model.addAttribute("today", LocalDate.now().toString());
            model.addAttribute("error",
                    "Could not save programme. Check the details and Nutrition Service.");

            return "programmes";
        }
    }
}