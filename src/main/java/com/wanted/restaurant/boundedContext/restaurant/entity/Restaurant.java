package com.wanted.restaurant.boundedContext.restaurant.entity;

import jakarta.persistence.Column;
import jakarta.persistence.ColumnResult;
import jakarta.persistence.ConstructorResult;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.NamedNativeQuery;
import jakarta.persistence.SqlResultSetMapping;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDate;

import static jakarta.persistence.GenerationType.IDENTITY;

import com.wanted.restaurant.boundedContext.restaurant.dto.LunchDTO;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder(toBuilder = true)
@DynamicUpdate
public class Restaurant {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String sigunName; // 시군명
    private String sigunCode; // 시군 코드
    private String businessPlaceName; // 사업장 명
    private LocalDate licenseBusinessDate; // 인허가 일자
    private Integer businessStatus; // 영업 상태 - 0 또는 1
    private LocalDate closeBusinessDate; // 폐업 일자
    private Double locationPlaceArea; // 소재지 면적
    private String grandFacilityDivisionName; // 급수 시설 구분 명
    private Integer maleEmployeeCount; // 남성 종사자 수
    private Integer year; // 년도
    private String multiUseBusiness; // 다중 이용 업소 여부
    private String grandDivisionName; // 등급 구분 명
    private Double totalFacilityScale; // 총시설규모(㎡)
    private Integer femaleEmployeeCount; // 여성 종사자 수
    private String businessClassificationDivisionName; // 영업장 주변 구분 명
    private String sanitationIndustryTypeName; // 위생업종명
    private String sanitationBusinessName; // 위생업태명
    private Integer totalEmployeesCount; // 총 종업원 수
    private String refinedLocationAddress; // 지번 주소
    private String refinedRoadNameAddress; // 도로명 주소
    private String refinedLocationZipCode; // 소재지우편번호
    private Double refinedWGS84Longitude; // WGS84경도
    private Double refinedWGS84Latitude; // WGS84위도

    private Double grade; // 평점
    @Column(unique=true)
    private String businessPlaceNameAndAddress; // 가게명 + 주소

    // 사용하는 데이터만 업데이트
    public void updateNewData(Restaurant restaurant) {
        this.sigunName = restaurant.getSigunName(); // 시군명 
        this.sigunCode = restaurant.getSigunCode(); // 시군 코드
        this.businessPlaceName = restaurant.getBusinessPlaceName(); // 사업장명
        this.businessStatus = restaurant.getBusinessStatus(); // 영업 상태
        this.refinedLocationAddress = restaurant.getRefinedLocationZipCode(); // 지번 주소
        this.refinedRoadNameAddress = restaurant.getRefinedRoadNameAddress(); // 도로명 주소
        this.refinedLocationZipCode = restaurant.getRefinedLocationZipCode(); // 우편번호
        this.refinedWGS84Longitude = restaurant.getRefinedWGS84Longitude(); // 경도
        this.refinedWGS84Latitude = restaurant.getRefinedWGS84Latitude(); // 위도
        this.businessPlaceNameAndAddress = restaurant.getBusinessPlaceNameAndAddress(); // 가게명 + 주소
        
    }

    public void updateGrade(double grade) {
        this.grade=grade;
    }
}
