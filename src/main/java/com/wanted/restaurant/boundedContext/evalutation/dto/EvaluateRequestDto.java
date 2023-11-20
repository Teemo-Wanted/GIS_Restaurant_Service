package com.wanted.restaurant.boundedContext.evalutation.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class EvaluateRequestDto {
    private long memberId;
    private long restaurantId;
    private int score;
    private String content;
}
