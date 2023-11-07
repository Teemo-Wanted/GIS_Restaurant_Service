package com.wanted.restaurant.boundedContext.restaurant.repository;

import com.wanted.restaurant.boundedContext.restaurant.dto.RestaurantQuery;
import com.wanted.restaurant.boundedContext.restaurant.entity.Restaurant;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RestaurantRepository extends JpaRepository<Restaurant,Long> {
  @Query(value = "select r.id as id, r.name as name, r.type as type, r.lat as lat, r.lng as lng, " +
          "ST_Distance_Sphere(POINT(r.lng,r.lat),POINT(lng,lat)) as distance " +
          "from Restaurant r where ST_Distance_Sphere(POINT(r.lng,r.lat),POINT(:lng, :lat))<=:maxDistance " +
          "order by ST_Distance_Sphere(POINT(r.lng,r.lat),POINT(lng,lat)) desc",nativeQuery = true)
  Slice<RestaurantQuery.RestaurantFeed> searchRestaurants(@Param(value = "lat")double lat, @Param(value = "lng")double lng,
                                           @Param(value = "maxDistance")double maxDistance, Pageable pageable);
}
