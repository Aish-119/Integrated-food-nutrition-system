package com.nutrition.nutrition.dto;
import java.time.LocalDate;
import java.util.List;
public record DistributionCreateRequest(
    Long beneficiaryId, LocalDate distributionDate,
    List<DistributionItemRequest> items) {}
