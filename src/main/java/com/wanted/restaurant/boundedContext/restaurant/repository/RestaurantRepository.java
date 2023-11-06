package com.wanted.restaurant.boundedContext.restaurant.repository;

import com.wanted.restaurant.boundedContext.restaurant.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
}
