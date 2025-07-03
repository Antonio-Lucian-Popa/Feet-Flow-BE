package com.asusoftware.feet_flow_api.subscription.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionDto {
    private UUID id;
    private UUID subscriberId;
    private UUID creatorId;
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean isActive;
}