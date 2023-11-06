package com.wanted.restaurant.util.openAPI.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum FoodType {
    CHIFOOD("Genrestrtchifood"),
    JAPNFOOD("Genrestrtjpnfood"),
    FASTFOOD("Genrestrtfastfood"),
    LUNCH("Genrestrtlunch");

    // 경기도 공공데이터 포털에서 open api를 호출할 때 사용할 url endpoint
    private final String endpoint;
}
