package com.wanted.restaurant.boundedContext.restaurant.service;

import com.wanted.restaurant.boundedContext.restaurant.dto.RestaurantQuery;
import com.wanted.restaurant.boundedContext.restaurant.dto.RestaurantQuery.RestaurantFeed;
import com.wanted.restaurant.boundedContext.restaurant.dto.RestaurantResponse;
import com.wanted.restaurant.boundedContext.restaurant.entity.Restaurant;
import com.wanted.restaurant.boundedContext.restaurant.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RestaurantService {
  private final RestaurantRepository restaurantRepository;

  public RestaurantResponse.RestaurantList search(double lat,double lng,double range,int page,int size){
    Slice<RestaurantFeed> restaurants = restaurantRepository.searchRestaurants(lat,lng,range,PageRequest.of(page,size));
    return RestaurantResponse.RestaurantList.of(restaurants);
  }
}
