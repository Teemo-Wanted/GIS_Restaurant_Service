package com.wanted.restaurant.util.openAPI.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import lombok.Getter;
@Getter
public class RestaurantOpenAPIDto {

    @JsonProperty("SIGUN_NM")
    @JsonSetter(nulls = Nulls.SKIP)
    private String sigunName; // 시군명

    @JsonProperty("SIGUN_CD")
    @JsonSetter(nulls = Nulls.SKIP)
    private String sigunCode; // 시군 코드

    @JsonProperty("BIZPLC_NM")
    @JsonSetter(nulls = Nulls.SKIP)
    private String businessPlaceName; // 사업장 명

    @JsonProperty("LICENSG_DE")
    @JsonSetter(nulls = Nulls.SKIP)
    private String licenseBusinessDate; // 인허가 일자

    @JsonProperty("BSN_STATE_NM")
    @JsonSetter(nulls = Nulls.SKIP)
    private String businessStatus; // 영업 상태 - 0 또는 1

    @JsonProperty("CLSBIZ_DE")
    @JsonSetter(nulls = Nulls.SKIP)
    private String closeBusinessDate; // 폐업 일자

    @JsonProperty("LOCPLC_AR")
    @JsonSetter(nulls = Nulls.SKIP)
    private Double locationPlaceArea = 0.0; // 소재지 면적

    @JsonProperty("GRAD_FACLT_DIV_NM")
    @JsonSetter(nulls = Nulls.SKIP)
    private String grandFacilityDivisionName; // 급수 시설 구분 명

    @JsonProperty("MALE_ENFLPSN_CNT")
    @JsonSetter(nulls = Nulls.SKIP)
    private Integer maleEmployeeCount; // 남성 종사자 수

    @JsonProperty("YY")
    @JsonSetter(nulls = Nulls.SKIP)
    private Integer year; // 년도

    @JsonProperty("MULTI_USE_BIZESTBL_YN")
    @JsonSetter(nulls = Nulls.SKIP)
    private String multiUseBusiness; // 다중 이용 업소 여부

    @JsonProperty("GRAD_DIV_NM")
    @JsonSetter(nulls = Nulls.SKIP)
    private String grandDivisionName; // 등급 구분 명

    @JsonProperty("TOT_FACLT_SCALE")
    @JsonSetter(nulls = Nulls.SKIP)
    private Double totalFacilityScale = 0.0; // 총시설규모(㎡)

    @JsonProperty("FEMALE_ENFLPSN_CNT")
    @JsonSetter(nulls = Nulls.SKIP)
    private Integer femaleEmployeeCount; // 여성 종사자 수

    @JsonProperty("BSNSITE_CIRCUMFR_DIV_NM")
    @JsonSetter(nulls = Nulls.SKIP)
    private String businessClassificationDivisionName; // 영업장 주변 구분 명

    @JsonProperty("SANITTN_INDUTYPE_NM")
    @JsonSetter(nulls = Nulls.SKIP)
    private String sanitationIndustryTypeName; // 위생업종명

    @JsonProperty("SANITTN_BIZCOND_NM")
    @JsonSetter(nulls = Nulls.SKIP)
    private String sanitationBusinessName; // 위생업태명

    @JsonProperty("TOT_EMPLY_CNT")
    @JsonSetter(nulls = Nulls.SKIP)
    private Integer totalEmployeesCount; // 총 종업원 수

    @JsonProperty("REFINE_LOTNO_ADDR")
    @JsonSetter(nulls = Nulls.SKIP)
    private String refinedLocationAddress; // 지번 주소

    @JsonProperty("REFINE_ROADNM_ADDR")
    @JsonSetter(nulls = Nulls.SKIP)
    private String refinedRoadNameAddress; // 도로명 주소

    @JsonProperty("REFINE_ZIP_CD")
    @JsonSetter(nulls = Nulls.SKIP)
    private String refinedLocationZipCode; // 소재지우편번호

    @JsonProperty("REFINE_WGS84_LOGT")
    @JsonSetter(nulls = Nulls.SKIP)
    private Double refinedWGS84Longitude = 0.0; // 경도

    @JsonProperty("REFINE_WGS84_LAT")
    @JsonSetter(nulls = Nulls.SKIP)
    private Double refinedWGS84Latitude = 0.0; // 위도
}
