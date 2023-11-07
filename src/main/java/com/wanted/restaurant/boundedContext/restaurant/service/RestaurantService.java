package com.wanted.restaurant.boundedContext.restaurant.service;

import com.wanted.restaurant.boundedContext.restaurant.dto.RestaurantQuery;
import com.wanted.restaurant.boundedContext.restaurant.dto.RestaurantQuery.RestaurantFeed;
import com.wanted.restaurant.boundedContext.restaurant.dto.RestaurantQuery.RestaurantFeedInterface;
import com.wanted.restaurant.boundedContext.restaurant.dto.RestaurantResponse;
import com.wanted.restaurant.boundedContext.restaurant.entity.Restaurant;
import com.wanted.restaurant.boundedContext.restaurant.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RestaurantService {
  private final RestaurantRepository restaurantRepository;

  public RestaurantResponse.RestaurantList search(double lat,double lng,double range,int page,int size){
    Slice<RestaurantFeedInterface> restaurantsInterfaces = restaurantRepository.searchRestaurants(lat,lng,range,PageRequest.of(page,size));
    Slice<RestaurantFeed> restaurants = toRestaurantFeed(restaurantsInterfaces);
    return RestaurantResponse.RestaurantList.of(restaurants);
  }
  private Slice<RestaurantFeed> toRestaurantFeed(Slice<RestaurantFeedInterface> feedInterfaces){
    List<RestaurantFeedInterface> interfaceList = feedInterfaces.toList();
    List<RestaurantFeed> feeds = interfaceList.stream().map(RestaurantFeed::of).toList();
    return new SliceImpl<>(feeds,feedInterfaces.getPageable(),feedInterfaces.hasNext());
  }
}
