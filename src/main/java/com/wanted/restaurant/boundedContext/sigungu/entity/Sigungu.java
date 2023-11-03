package com.wanted.restaurant.boundedContext.sigungu.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Sigungu{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String dosi;//도시

    private String sigungu; //시군구

    private Double longitude; // 경도

    private Double latitude; // 위도

    public Sigungu(String dosi, String sigungu, double longitude, double latitude) {
        this.dosi = dosi;
        this.sigungu = sigungu;
        this.longitude = longitude;
        this.latitude =latitude;
    }
}
