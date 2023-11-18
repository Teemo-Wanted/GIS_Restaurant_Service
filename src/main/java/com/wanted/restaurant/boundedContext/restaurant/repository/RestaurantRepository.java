package com.wanted.restaurant.boundedContext.restaurant.repository;

import com.wanted.restaurant.boundedContext.restaurant.dto.RestaurantQuery;
import com.wanted.restaurant.boundedContext.restaurant.entity.Restaurant;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;


public interface RestaurantRepository extends JpaRepository<Restaurant,Long> {
  @Query(value = "select r.id as id, r.name as name, r.type as type, r.lat as lat, r.lng as lng, " +
          "ST_Distance_Sphere(POINT(r.lng,r.lat),POINT(:lng,:lat)) as distance " +
          "from Restaurant r where ST_Distance_Sphere(POINT(r.lng,r.lat),POINT(:lng, :lat))<=:maxDistance " +
          "order by ST_Distance_Sphere(POINT(r.lng,r.lat),POINT(lng,lat)) desc",nativeQuery = true)
  Slice<RestaurantQuery.RestaurantFeedInterface> searchRestaurants(@Param(value = "lat")double lat, @Param(value = "lng")double lng,
                                           @Param(value = "maxDistance")double maxDistance, Pageable pageable);
  List<Restaurant> findBySanitationBusinessName(String sanitationBusinessName);

  Optional<Restaurant> findByBusinessPlaceNameAndRefinedLocationAddress(String businessPlaceName, String finedLocationAddress);

  @Query(value = "select a.* from (select round(avg(score),1)" +
          " from evaluation join restaurant on evaluation.restaurant_id = restaurant.id" +
          " where restaurant.business_place_name=:placeName ) as a",nativeQuery = true)
  Double getAvg(@Param(value = "placeName") String businessPlaceName);
}
