package com.nutrition.nutrition.service;

import com.nutrition.nutrition.dto.*;
import com.nutrition.nutrition.entity.*;
import com.nutrition.nutrition.repository.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@Service
public class DistributionService {

    private static final Logger log = LoggerFactory.getLogger(DistributionService.class);

    private static final String INVENTORY_URL = "http://localhost:8081/api/inventory-batches";

    private final DistributionRepository distributions;
    private final DistributionItemRepository items;
    private final BeneficiaryRepository beneficiaries;
    private final RestTemplate restTemplate = new RestTemplate();

    public DistributionService(
            DistributionRepository distributions,
            DistributionItemRepository items,
            BeneficiaryRepository beneficiaries) {

        this.distributions = distributions;
        this.items = items;
        this.beneficiaries = beneficiaries;
    }

    private record StockKey(Long foodItemId, String unit) {
    }

    private record BatchUpdate(
            Long batchId,
            Map<String, Object> originalBody,
            Map<String, Object> updatedBody) {
    }

    // --------------------------------------------------
    // GET ALL DISTRIBUTIONS
    // --------------------------------------------------

    public List<Distribution> getAll() {
        return distributions.findAll();
    }

    // --------------------------------------------------
    // GET ITEMS IN A DISTRIBUTION
    // --------------------------------------------------

    public List<DistributionItem> getItems(Long distributionId) {

        if (!distributions.existsById(distributionId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Distribution not found");
        }

        return items.findByDistributionId(distributionId);
    }

    // --------------------------------------------------
    // FETCH INVENTORY FROM FOOD BACKEND
    // --------------------------------------------------

    private List<Map<String, Object>> fetchInventory() {

        try {
            List<Map<String, Object>> batches = restTemplate.exchange(
                    INVENTORY_URL,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<Map<String, Object>>>() {
                    }).getBody();

            return batches != null ? batches : new ArrayList<>();

        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_GATEWAY,
                    "Food Backend inventory API is unavailable",
                    e);
        }
    }

    // --------------------------------------------------
    // HELPER: GET FOOD ITEM ID FROM INVENTORY BATCH
    // --------------------------------------------------

    private Long getFoodItemId(Map<String, Object> batch) {

        Object foodObject = batch.get("foodItem");

        if (foodObject instanceof Map<?, ?> food) {
            Object id = food.get("id");

            if (id != null) {
                return Long.valueOf(String.valueOf(id));
            }
        }

        Object directId = batch.get("foodItemId");

        return directId == null
                ? null
                : Long.valueOf(String.valueOf(directId));
    }

    // --------------------------------------------------
    // HELPER: READ EXPIRY DATE
    // --------------------------------------------------

    private LocalDate getExpiryDate(Map<String, Object> batch) {

        Object value = batch.get("expiryDate");

        if (value == null || String.valueOf(value).isBlank()) {
            return null;
        }

        return LocalDate.parse(String.valueOf(value));
    }

    // --------------------------------------------------
    // HELPER: READ QUANTITY
    // --------------------------------------------------

    private BigDecimal getQuantity(Map<String, Object> batch) {

        Object value = batch.get("quantity");

        if (value == null) {
            return BigDecimal.ZERO;
        }

        return new BigDecimal(String.valueOf(value));
    }

    // --------------------------------------------------
    // VALIDATE AND COMBINE REQUESTED ITEMS
    // --------------------------------------------------

    private List<DistributionItemRequest> aggregateRequests(
            List<DistributionItemRequest> requested) {

        if (requested == null || requested.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Add at least one food item");
        }

        Map<StockKey, BigDecimal> totals = new LinkedHashMap<>();

        for (DistributionItemRequest request : requested) {

            if (request.foodItemId() == null
                    || request.quantity() == null
                    || request.quantity().signum() <= 0
                    || request.unit() == null
                    || request.unit().isBlank()) {

                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Each item needs a food item ID, positive quantity and unit");
            }

            String unit = request.unit()
                    .trim()
                    .toUpperCase(Locale.ROOT);

            StockKey key = new StockKey(
                    request.foodItemId(), unit);

            totals.merge(
                    key,
                    request.quantity(),
                    BigDecimal::add);
        }

        List<DistributionItemRequest> result = new ArrayList<>();

        totals.forEach((key, quantity) -> result.add(new DistributionItemRequest(
                key.foodItemId(),
                quantity,
                key.unit())));

        return result;
    }

    // --------------------------------------------------
    // CHECK AVAILABLE STOCK
    // --------------------------------------------------

    public List<StockCheckResult> checkStock(
            List<DistributionItemRequest> requested) {

        List<DistributionItemRequest> combined = aggregateRequests(requested);

        List<Map<String, Object>> batches = fetchInventory();

        LocalDate today = LocalDate.now();
        LocalDate nextWeek = today.plusDays(7);

        List<StockCheckResult> results = new ArrayList<>();

        for (DistributionItemRequest request : combined) {

            BigDecimal available = BigDecimal.ZERO;
            BigDecimal expiring = BigDecimal.ZERO;

            for (Map<String, Object> batch : batches) {

                Long batchFoodItemId = getFoodItemId(batch);

                if (!request.foodItemId().equals(batchFoodItemId)) {
                    continue;
                }

                if (!request.unit().equalsIgnoreCase(
                        String.valueOf(batch.get("unit")))) {
                    continue;
                }

                LocalDate expiry = getExpiryDate(batch);

                // Do not count expired stock.
                if (expiry != null && expiry.isBefore(today)) {
                    continue;
                }

                BigDecimal quantity = getQuantity(batch);

                if (quantity.signum() <= 0) {
                    continue;
                }

                available = available.add(quantity);

                if (expiry != null
                        && !expiry.isAfter(nextWeek)) {
                    expiring = expiring.add(quantity);
                }
            }

            String status;

            if (available.compareTo(request.quantity()) < 0) {
                status = "SHORTAGE";

            } else if (expiring.signum() > 0
                    && available.subtract(expiring)
                            .compareTo(request.quantity()) < 0) {
                status = "EXPIRING_STOCK";

            } else {
                status = "OK";
            }

            results.add(new StockCheckResult(
                    request.foodItemId(),
                    request.unit(),
                    request.quantity(),
                    available,
                    expiring,
                    status));
        }

        return results;
    }

    // --------------------------------------------------
    // PREPARE STOCK DEDUCTIONS
    // Uses earliest-expiring eligible batches first.
    // No inventory changes occur in this method.
    // --------------------------------------------------

    private List<BatchUpdate> prepareStockUpdates(
            List<DistributionItemRequest> requested) {

        List<DistributionItemRequest> combined = aggregateRequests(requested);

        List<Map<String, Object>> batches = fetchInventory();

        LocalDate today = LocalDate.now();
        List<BatchUpdate> updates = new ArrayList<>();

        for (DistributionItemRequest request : combined) {

            List<Map<String, Object>> eligible = new ArrayList<>();

            for (Map<String, Object> batch : batches) {

                Long batchFoodItemId = getFoodItemId(batch);

                if (!request.foodItemId().equals(batchFoodItemId)) {
                    continue;
                }

                if (!request.unit().equalsIgnoreCase(
                        String.valueOf(batch.get("unit")))) {
                    continue;
                }

                LocalDate expiry = getExpiryDate(batch);

                if (expiry != null && expiry.isBefore(today)) {
                    continue;
                }

                if (getQuantity(batch).signum() > 0) {
                    eligible.add(batch);
                }
            }

            eligible.sort(
                    Comparator.comparing(
                            this::getExpiryDate,
                            Comparator.nullsLast(
                                    Comparator.naturalOrder())));

            BigDecimal remaining = request.quantity();

            for (Map<String, Object> batch : eligible) {

                if (remaining.signum() <= 0) {
                    break;
                }

                BigDecimal current = getQuantity(batch);

                BigDecimal deduction = current.min(remaining);

                BigDecimal newQuantity = current.subtract(deduction);

                Long batchId = Long.valueOf(
                        String.valueOf(batch.get("id")));

                Long foodItemId = getFoodItemId(batch);

                Map<String, Object> originalBody = createBatchRequest(
                        batch,
                        foodItemId,
                        current);

                Map<String, Object> updatedBody = createBatchRequest(
                        batch,
                        foodItemId,
                        newQuantity);

                updates.add(new BatchUpdate(
                        batchId,
                        originalBody,
                        updatedBody));

                remaining = remaining.subtract(deduction);
            }

            if (remaining.signum() > 0) {
                throw new ResponseStatusException(
                        HttpStatus.CONFLICT,
                        "Insufficient stock for food item "
                                + request.foodItemId());
            }
        }

        return updates;
    }

    // --------------------------------------------------
    // BUILD THE REQUEST BODY EXPECTED BY FOOD BACKEND
    // --------------------------------------------------

    private Map<String, Object> createBatchRequest(
            Map<String, Object> batch,
            Long foodItemId,
            BigDecimal quantity) {

        Map<String, Object> body = new LinkedHashMap<>();

        body.put("foodItemId", foodItemId);
        body.put("batchNumber", batch.get("batchNumber"));
        body.put("quantity", quantity);
        body.put("unit", batch.get("unit"));
        body.put("receivedDate", batch.get("receivedDate"));
        body.put("expiryDate", batch.get("expiryDate"));

        return body;
    }

    // --------------------------------------------------
    // APPLY INVENTORY UPDATES
    // Attempts to restore earlier changes if an update fails.
    // --------------------------------------------------

    private void applyStockUpdates(List<BatchUpdate> updates) {

        List<BatchUpdate> applied = new ArrayList<>();

        try {
            for (BatchUpdate update : updates) {

                restTemplate.put(
                        INVENTORY_URL + "/" + update.batchId(),
                        update.updatedBody());

                applied.add(update);
            }

        } catch (Exception e) {

            restoreStock(applied);

            throw new ResponseStatusException(
                    HttpStatus.BAD_GATEWAY,
                    "Could not update inventory. Check the Food Backend.",
                    e);
        }
    }

    // --------------------------------------------------
    // RESTORE INVENTORY IF DISTRIBUTION SAVE FAILS
    // --------------------------------------------------

    private void restoreStock(List<BatchUpdate> updates) {

        List<BatchUpdate> reverse = new ArrayList<>(updates);
        Collections.reverse(reverse);

        for (BatchUpdate update : reverse) {

            try {
                restTemplate.put(
                        INVENTORY_URL + "/" + update.batchId(),
                        update.originalBody());

            } catch (Exception e) {
                log.error(
                        "Could not restore inventory batch {}. "
                                + "Manual inventory verification is required.",
                        update.batchId(),
                        e);
            }
        }
    }

    // --------------------------------------------------
    // CREATE DISTRIBUTION AND DEDUCT INVENTORY
    // --------------------------------------------------

    @Transactional
    public Distribution create(DistributionCreateRequest request) {

        if (request == null || request.beneficiaryId() == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "beneficiaryId is required");
        }

        Beneficiary beneficiary = beneficiaries.findById(request.beneficiaryId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Beneficiary not found"));

        List<StockCheckResult> checks = checkStock(request.items());

        if (checks.stream().anyMatch(
                result -> "SHORTAGE".equals(result.status()))) {

            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Insufficient inventory for one or more requested items");
        }

        // Prepare all deductions before changing stock.
        List<BatchUpdate> updates = prepareStockUpdates(request.items());

        // Deduct stock through the Food Backend API.
        applyStockUpdates(updates);

        try {
            Distribution distribution = new Distribution();

            distribution.setBeneficiary(beneficiary);

            distribution.setDistributionDate(
                    request.distributionDate() == null
                            ? LocalDate.now()
                            : request.distributionDate());

            distribution.setStatus("PLANNED");

            distribution = distributions.save(distribution);

            for (DistributionItemRequest input : request.items()) {

                DistributionItem item = new DistributionItem();

                item.setDistribution(distribution);
                item.setFoodItemId(input.foodItemId());
                item.setQuantity(input.quantity());
                item.setUnit(input.unit());

                items.save(item);
            }

            return distribution;

        } catch (RuntimeException e) {

            // Restore inventory if saving the database records fails.
            restoreStock(updates);
            throw e;
        }
    }

    // --------------------------------------------------
    // UPDATE DISTRIBUTION STATUS
    // --------------------------------------------------

    public Distribution updateStatus(
            Long id,
            String newStatus) {

        Distribution distribution = distributions.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Distribution not found"));

        if (newStatus == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Status is required");
        }

        String next = newStatus.toUpperCase(Locale.ROOT);

        if (!Set.of(
                "PLANNED",
                "IN_PROGRESS",
                "COMPLETED",
                "CANCELLED").contains(next)) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Invalid status");
        }

        String current = distribution.getStatus();

        boolean allowed = current.equals(next)
                || ("PLANNED".equals(current)
                        && Set.of(
                                "IN_PROGRESS",
                                "CANCELLED").contains(next))
                || ("IN_PROGRESS".equals(current)
                        && Set.of(
                                "COMPLETED",
                                "CANCELLED").contains(next));

        if (!allowed) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Invalid distribution status transition: "
                            + current + " to " + next);
        }

        distribution.setStatus(next);

        return distributions.save(distribution);
    }
}