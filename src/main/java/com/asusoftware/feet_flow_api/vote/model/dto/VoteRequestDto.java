package com.asusoftware.feet_flow_api.vote.model.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VoteRequestDto {
    @NotNull
    private UUID postId;

    @NotNull
    @Min(-1)
    @Max(1)
    private Integer value;
}
