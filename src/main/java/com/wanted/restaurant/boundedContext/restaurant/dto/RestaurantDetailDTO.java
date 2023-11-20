package com.wanted.restaurant.boundedContext.restaurant.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.wanted.restaurant.boundedContext.evalutation.entity.Evaluation;
import com.wanted.restaurant.boundedContext.restaurant.entity.Restaurant;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantDetailDTO {
	private Long RestaurantId;

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
	private String businessPlaceNameAndAddress; // 가게명 + 주소

	private List<Evaluation> evaluations = new ArrayList<>();

	public RestaurantDetailDTO(Restaurant restaurant, List<Evaluation> evaluations) {
		this.RestaurantId = restaurant.getId();
		this.sigunName = restaurant.getSigunName(); // 시군명
		this.sigunCode = restaurant.getSigunCode(); // 시군 코드
		this.businessPlaceName = restaurant.getBusinessPlaceName(); // 사업장 명
		this.licenseBusinessDate = restaurant.getLicenseBusinessDate(); // 인허가 일자
		this.businessStatus = restaurant.getBusinessStatus(); // 영업 상태 - 0 또는 1
		this.closeBusinessDate = restaurant.getCloseBusinessDate(); // 폐업 일자
		this.locationPlaceArea = restaurant.getLocationPlaceArea(); // 소재지 면적
		this.grandFacilityDivisionName = restaurant.getGrandFacilityDivisionName(); // 급수 시설 구분 명
		this.maleEmployeeCount = restaurant.getMaleEmployeeCount(); // 남성 종사자 수
		this.year = restaurant.getYear(); // 년도
		this.multiUseBusiness = restaurant.getMultiUseBusiness(); // 다중 이용 업소 여부
		this.grandDivisionName = restaurant.getGrandDivisionName(); // 등급 구분 명
		this.totalFacilityScale = restaurant.getTotalFacilityScale(); // 총시설규모(㎡)
		this.femaleEmployeeCount = restaurant.getFemaleEmployeeCount(); // 여성 종사자 수
		this.businessClassificationDivisionName = restaurant.getBusinessClassificationDivisionName(); // 영업장 주변 구분 명
		this.sanitationIndustryTypeName = restaurant.getSanitationIndustryTypeName(); // 위생업종명
		this.sanitationBusinessName = restaurant.getSanitationBusinessName(); // 위생업태명
		this.totalEmployeesCount = restaurant.getTotalEmployeesCount(); // 총 종업원 수
		this.refinedLocationAddress = restaurant.getRefinedLocationAddress(); // 지번 주소
		this.refinedRoadNameAddress = restaurant.getRefinedRoadNameAddress(); // 도로명 주소
		this.refinedLocationZipCode = restaurant.getRefinedLocationZipCode(); // 소재지우편번호
		this.refinedWGS84Longitude = restaurant.getRefinedWGS84Longitude(); // WGS84경도
		this.refinedWGS84Latitude = restaurant.getRefinedWGS84Latitude(); // WGS84위도
		this.grade = restaurant.getGrade(); // 평점
		this.businessPlaceNameAndAddress = restaurant.getBusinessPlaceNameAndAddress(); // 가게명 + 주소
		this.evaluations = evaluations;
	}
}
