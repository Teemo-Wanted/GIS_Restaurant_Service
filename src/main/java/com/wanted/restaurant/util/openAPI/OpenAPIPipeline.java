package com.wanted.restaurant.util.openAPI;

import com.wanted.restaurant.boundedContext.restaurant.entity.Restaurant;
import com.wanted.restaurant.boundedContext.restaurant.repository.RestaurantRepository;
import com.wanted.restaurant.util.openAPI.dto.RestaurantOpenAPIDto;
import com.wanted.restaurant.util.openAPI.entity.FoodType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class OpenAPIPipeline {

    private final OpenAPIReader openAPIReader;
    @Autowired
    private RestaurantRepository restaurantRepository;

    @Transactional
    public void pipeline() throws IOException, ParseException {
        for(FoodType foodType : FoodType.values()) {
            String endpoint = foodType.getEndpoint();
            // 1. 데이터 수집
            List<RestaurantOpenAPIDto> restaurantOpenAPIDtoList = openAPIReader.readOpenAPIData(endpoint);

            // 2. 데이터 전처리
            List<Restaurant> restaurants = dataPreprecessing(restaurantOpenAPIDtoList);

            // 3. 데이터 저장
            saveOrUpdate(restaurants, foodType);
        }
    }

    private void saveOrUpdate(List<Restaurant> restaurants, FoodType foodType) {
        List<Restaurant> savedRestaurants = restaurantRepository.findBySanitationBusinessName(foodType.getSanitationBusinessName());
        if(savedRestaurants.isEmpty()) {
            restaurantRepository.saveAll(restaurants);
            return;
        }

        for(Restaurant restaurant : restaurants) {
            updateData(restaurant);
        }

    }

    // [[[ 데이터 갱신 ]]]
    // 1. 음식점 있다면, 바뀐 값만 확인 후 update >>> @DynamicUpdate 활용
    // 2. 음식점 없다면, insert
    // *조회는 Unique key 조합인 businessPlaceName + refinedLocationAddress 으로 한다. (주소가 바뀌 었을 때 조회할 수 없는 한계가 있음.)
    private void updateData(Restaurant restaurant) {
        Optional<Restaurant> optionalRestaurant = restaurantRepository.findByBusinessPlaceNameAndRefinedLocationAddress(restaurant.getBusinessPlaceName(), restaurant.getRefinedLocationAddress());
        if(optionalRestaurant.isEmpty()) { // 값이 없다.
            restaurantRepository.save(restaurant);
            return;
        }

        Restaurant savedRestaurant = optionalRestaurant.get();
        savedRestaurant.updateNewData(restaurant);
    }

    private List<Restaurant> dataPreprecessing(List<RestaurantOpenAPIDto> restaurantOpenAPIDtoList) {
        List<Restaurant> restaurants = new ArrayList<>();

        for(RestaurantOpenAPIDto restaurantOpenAPIDto : restaurantOpenAPIDtoList) {

            log.info(">>>>>>>>>>>>>>>>> RestaurantDto " + restaurantOpenAPIDto.getBusinessPlaceName() + " <<<<<<<<<<<<<<<");
            log.info("  - sigunName : " + restaurantOpenAPIDto.getSigunName());
            log.info("  - sigunCode : " + restaurantOpenAPIDto.getSigunCode());
            log.info("  - businessPlaceName : " + restaurantOpenAPIDto.getBusinessPlaceName());
            log.info("  - licenseBusinessDate : " + restaurantOpenAPIDto.getLicenseBusinessDate());
            log.info("  - businessStatus : " + restaurantOpenAPIDto.getBusinessStatus());
            log.info("  - closeBusinessDate : " + restaurantOpenAPIDto.getCloseBusinessDate());
            log.info("  - locationPlaceArea : " + restaurantOpenAPIDto.getLocationPlaceArea());
            log.info("  - grandFacilityDivisionName : " + restaurantOpenAPIDto.getGrandDivisionName());
            log.info("  - maleEmployeeCount : " + restaurantOpenAPIDto.getMaleEmployeeCount());
            log.info("  - year : " + restaurantOpenAPIDto.getYear());
            log.info("  - multiUseBusiness : " + restaurantOpenAPIDto.getMultiUseBusiness());
            log.info("  - grandDivisionName : " + restaurantOpenAPIDto.getGrandDivisionName());
            log.info("  - totalFacilityScale : " + restaurantOpenAPIDto.getTotalFacilityScale());
            log.info("  - femaleEmployeeCount : " + restaurantOpenAPIDto.getFemaleEmployeeCount());
            log.info("  - businessClassificationDivisionName : " + restaurantOpenAPIDto.getBusinessClassificationDivisionName());
            log.info("  - sanitationIndustryTypeName : " + restaurantOpenAPIDto.getSanitationIndustryTypeName());
            log.info("  - sanitationBusinessName : " + restaurantOpenAPIDto.getSanitationBusinessName());
            log.info("  - refinedLocationAddress : " + restaurantOpenAPIDto.getRefinedLocationAddress());
            log.info("  - refinedRoadNameAddress : " + restaurantOpenAPIDto.getRefinedRoadNameAddress());
            log.info("  - refinedLocationZipCode : " + restaurantOpenAPIDto.getRefinedLocationZipCode());
            log.info("  - refinedWGS84Longitude : " + restaurantOpenAPIDto.getRefinedWGS84Longitude());
            log.info("  - refinedWGS84Latitude : " + restaurantOpenAPIDto.getRefinedWGS84Latitude());

            Restaurant restaurant = createRestaurant(restaurantOpenAPIDto);

            restaurants.add(restaurant);
        }

        return restaurants;

    }

    private Restaurant createRestaurant(RestaurantOpenAPIDto restaurantOpenAPIDto) {
        // String to LocalDate
        LocalDate licenseBusinessDate = toLocalDate(restaurantOpenAPIDto.getLicenseBusinessDate()); // 인허가 일자
        LocalDate closeBusinessDate = toLocalDate(restaurantOpenAPIDto.getCloseBusinessDate()); // 폐업 일자

        // 영업 상태 String to Integer
        Integer businessStatus = businessStatusToInteger(restaurantOpenAPIDto.getBusinessStatus());

        // 엔티티 생성
        return Restaurant.builder()
                .sigunName(restaurantOpenAPIDto.getSigunName())
                .sigunCode(restaurantOpenAPIDto.getSigunCode())
                .businessPlaceName(restaurantOpenAPIDto.getBusinessPlaceName())
                .licenseBusinessDate(licenseBusinessDate)
                .businessStatus(businessStatus)
                .closeBusinessDate(closeBusinessDate)
                .locationPlaceArea(restaurantOpenAPIDto.getLocationPlaceArea())
                .grandFacilityDivisionName(restaurantOpenAPIDto.getGrandFacilityDivisionName())
                .maleEmployeeCount(restaurantOpenAPIDto.getMaleEmployeeCount())
                .year(restaurantOpenAPIDto.getYear())
                .multiUseBusiness(restaurantOpenAPIDto.getMultiUseBusiness())
                .grandDivisionName(restaurantOpenAPIDto.getGrandDivisionName())
                .totalFacilityScale(restaurantOpenAPIDto.getTotalFacilityScale())
                .femaleEmployeeCount(restaurantOpenAPIDto.getFemaleEmployeeCount())
                .businessClassificationDivisionName(restaurantOpenAPIDto.getBusinessClassificationDivisionName())
                .sanitationIndustryTypeName(restaurantOpenAPIDto.getSanitationIndustryTypeName())
                .sanitationBusinessName(restaurantOpenAPIDto.getSanitationBusinessName())
                .totalEmployeesCount(restaurantOpenAPIDto.getTotalEmployeesCount())
                .refinedLocationAddress(restaurantOpenAPIDto.getRefinedLocationAddress())
                .refinedRoadNameAddress(restaurantOpenAPIDto.getRefinedRoadNameAddress())
                .refinedLocationZipCode(restaurantOpenAPIDto.getRefinedLocationZipCode())
                .refinedWGS84Longitude(restaurantOpenAPIDto.getRefinedWGS84Longitude())
                .refinedWGS84Latitude(restaurantOpenAPIDto.getRefinedWGS84Latitude())
                .grade(0.0)
                .businessPlaceNameAndAddress(restaurantOpenAPIDto.getBusinessPlaceName() + restaurantOpenAPIDto.getRefinedLocationAddress())
                .build();

    }

    private LocalDate toLocalDate(String strDate) {
        if(strDate == null) return LocalDate.of(9999, 12, 31);

        if(!strDate.contains("-")) {
            String year = strDate.substring(0, 4);
            log.info(year);
            String month = strDate.substring(4, 6);
            String day = strDate.substring(6);

            strDate = year + "-" + month + "-" + day;
        }

        return LocalDate.parse(strDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    private Integer businessStatusToInteger(String strBusinessStatus) {
        return strBusinessStatus.equals("영업")? 1 : 0;
    }
}
