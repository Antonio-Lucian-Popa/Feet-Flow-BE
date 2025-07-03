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
public class SubscriptionCheckDto {
    private UUID id;
    private boolean isActive;
    private LocalDate endDate;
}