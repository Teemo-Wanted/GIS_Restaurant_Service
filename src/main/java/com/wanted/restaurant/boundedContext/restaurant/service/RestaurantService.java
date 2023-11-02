package com.wanted.restaurant.boundedContext.restaurant.service;

import com.wanted.restaurant.boundedContext.restaurant.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RestaurantService {
  private final RestaurantRepository restaurantRepository;
}
