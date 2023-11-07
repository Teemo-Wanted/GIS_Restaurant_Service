package com.wanted.restaurant.boundedContext.restaurant.repository;

import com.wanted.restaurant.boundedContext.restaurant.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
    List<Restaurant> findBySanitationBusinessName(String sanitationBusinessName);
}
