package com.wanted.restaurant.boundedContext.restaurant.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.wanted.restaurant.boundedContext.restaurant.dto.LunchDTO;
import com.wanted.restaurant.boundedContext.restaurant.dto.RestaurantQuery;
import com.wanted.restaurant.boundedContext.restaurant.entity.Restaurant;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
	@Query(value =
		"select r.id as id, r.business_place_name as name, r.sanitation_business_name as type, r.refinedwgs84latitude as lat, r.refinedwgs84longitude as lng, r.grade as grade,"
			+
			"ST_Distance_Sphere(POINT(r.refinedwgs84longitude,r.refinedwgs84latitude),POINT(:lng,:lat)) as distance " +
			"from Restaurant r where ST_Distance_Sphere(POINT(r.refinedwgs84longitude,r.refinedwgs84latitude),POINT(:lng, :lat))<=:maxDistance "
			+
			"order by CASE WHEN :orderType = 'score' "
			+ "THEN r.grade END desc, "
			+ "CASE WHEN :orderType IS NULL OR :orderType != 'score' "
			+ "THEN ST_Distance_Sphere(POINT(r.refinedwgs84longitude,r.refinedwgs84latitude),POINT(:lng,:lat)) END asc", nativeQuery = true)
	Slice<RestaurantQuery.RestaurantFeedInterface> searchRestaurants(@Param(value = "lat") double lat,
		@Param(value = "lng") double lng,
		@Param(value = "maxDistance") double maxDistance, @Param(value = "orderType") String orderType,
		Pageable pageable);

	List<Restaurant> findBySanitationBusinessName(String sanitationBusinessName);

	Optional<Restaurant> findByBusinessPlaceNameAndRefinedLocationAddress(String businessPlaceName,
		String finedLocationAddress);

	@Query(value = "select a.* from (select round(avg(score),1)" +
		" from evaluation join restaurant on evaluation.restaurant_id = restaurant.id" +
		" where restaurant.business_place_name=:placeName ) as a", nativeQuery = true)
	Double getAvg(@Param(value = "placeName") String businessPlaceName);

	@Query(value = "SELECT t.name AS name, t.address AS address, t.grade AS grade, t.type AS type FROM (" +
		"SELECT r.business_place_name AS name, r.refined_road_name_address AS address, r.grade AS grade, r.sanitation_business_name AS type, "
		+
		"ROW_NUMBER() OVER(PARTITION BY r.sanitation_business_name ORDER BY r.grade DESC) as rn " +
		"FROM Restaurant r WHERE ST_Distance_Sphere(POINT(r.refinedwgs84longitude,r.refinedwgs84latitude),POINT(:lng, :lat))<=:maxDistance "
		+
		") t " +
		"WHERE t.rn <= 3", nativeQuery = true)
	List<LunchDTO.LunchDTOInterface> getRestaurantByCategoryTop3(@Param(value = "lat") double lat,
		@Param(value = "lng") double lng, @Param(value = "maxDistance") double maxDistance);
}
