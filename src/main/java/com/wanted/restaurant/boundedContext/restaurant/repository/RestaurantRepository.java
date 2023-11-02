package com.wanted.restaurant.boundedContext.restaurant.repository;

import com.wanted.restaurant.boundedContext.restaurant.entity.Restaurant;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RestaurantRepository extends JpaRepository<Restaurant,Long> {
  @Query(value = "select r from Restaurant r where ST_Distance_Sphere(POINT(r.lat,r.lng),POINT(:lat,:lng))<=:maxDistance " +
          "order by ST_Distance_Sphere(POINT(r.lat,r.lng),POINT(lat,lng)) desc",nativeQuery = true)
  Slice<Restaurant> searchRestaurants(@Param(value = "lat")double lat, @Param(value = "lng")double lng,
                                      @Param(value = "maxDistance")double maxDistance, Pageable pageable);
}
