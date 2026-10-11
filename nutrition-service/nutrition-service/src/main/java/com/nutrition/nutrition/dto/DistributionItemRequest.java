package com.nutrition.nutrition.dto;
import java.math.BigDecimal;
public record DistributionItemRequest(Long foodItemId, BigDecimal quantity, String unit) {}
