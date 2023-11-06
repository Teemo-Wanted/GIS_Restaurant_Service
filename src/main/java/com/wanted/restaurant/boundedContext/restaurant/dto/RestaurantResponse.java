package com.wanted.restaurant.boundedContext.restaurant.dto;

import com.wanted.restaurant.boundedContext.restaurant.entity.Restaurant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Slice;

import java.util.List;
import java.util.stream.Collectors;

public class RestaurantResponse {
  @AllArgsConstructor
  @NoArgsConstructor
  @Getter
  public static class RestaurantList{
    private int page;
    private int size;
    private boolean hasNext;
    private List<RestaurantListElement> restaurants;
    public static RestaurantList of(Slice<Restaurant> restaurants){
      int page = restaurants.getPageable().getPageNumber();
      int size = restaurants.getPageable().getPageSize();
      boolean hasNext = restaurants.hasNext();
      List<RestaurantListElement> restaurantElements = restaurants.getContent().stream()
              .map(RestaurantListElement::of).toList();
      return new RestaurantList(page,size,hasNext,restaurantElements);
    }
  }

  @AllArgsConstructor
  @NoArgsConstructor
  @Getter
  public static class RestaurantListElement{
    private long id;
    private String name;
    private String type;
    private double distance;
    private double lat;
    private double lng;
    public static RestaurantListElement of(Restaurant r){
      return new RestaurantListElement(r.getId(),r.getName(),r.getType(),0.1D,r.getLat(),r.getLng());
    }
  }
}
