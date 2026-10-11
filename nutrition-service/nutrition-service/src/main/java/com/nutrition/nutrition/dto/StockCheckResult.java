package com.nutrition.nutrition.dto;
import java.math.BigDecimal;
public record StockCheckResult(
    Long foodItemId, String unit, BigDecimal requestedQuantity,
    BigDecimal availableQuantity, BigDecimal expiringWithin7Days, String status) {}
