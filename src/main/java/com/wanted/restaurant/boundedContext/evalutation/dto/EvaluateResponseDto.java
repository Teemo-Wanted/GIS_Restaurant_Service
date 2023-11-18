package com.wanted.restaurant.boundedContext.evalutation.dto;

import com.wanted.restaurant.boundedContext.evalutation.entity.Evaluation;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class EvaluateResponseDto {
    private String account;
    private String restaurantName;
    private int score;
    private String content;

    public EvaluateResponseDto(Evaluation evaluation){
        this.account=evaluation.getMember().getAccount();
        this.restaurantName=evaluation.getRestaurant().getBusinessPlaceName();
        this.score=evaluation.getScore();
        this.content=evaluation.getContent();
    }
}
