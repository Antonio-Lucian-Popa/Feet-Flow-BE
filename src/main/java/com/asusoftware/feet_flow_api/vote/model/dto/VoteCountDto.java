package com.asusoftware.feet_flow_api.vote.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VoteCountDto {
    private int upVotes;
    private int downVotes;
}
