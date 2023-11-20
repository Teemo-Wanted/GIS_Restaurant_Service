package com.wanted.restaurant.boundedContext.evalutation.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class EvaluateRequestDto {
    private long memberId;
    private long restaurantId;
    private int score;
    private String content;
}
