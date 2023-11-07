package com.wanted.restaurant.util.openAPI.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum FoodType {
    CHIFOOD("Genrestrtchifood", "중국식"),
    JAPNFOOD("Genrestrtjpnfood", "일식"),
    LUNCH("Genrestrtlunch", "김밥(도시락)");

    private final String endpoint; // 경기도 공공데이터 포털에서 open api를 호출할 때 사용할 url endpoint
    private final String sanitationBusinessName; // 타입별 음식점 저장 여부 확인할 때 사용할 음식점 타입 이름
}
