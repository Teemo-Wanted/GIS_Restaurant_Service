package com.wanted.restaurant.boundedContext.restaurant.dto;

import com.wanted.restaurant.boundedContext.restaurant.dto.RestaurantQuery.RestaurantFeed;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Slice;

import java.util.List;

public class RestaurantResponse {
  @AllArgsConstructor
  @NoArgsConstructor
  @Getter
  public static class RestaurantList{
    private int page;
    private int size;
    private boolean hasNext;
    private List<RestaurantListElement> restaurants;
    public static RestaurantList of(Slice<RestaurantFeed> restaurants){
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

    private double grade;
    public static RestaurantListElement of(RestaurantFeed r){
      return new RestaurantListElement(r.getId(),r.getName(),r.getType(),r.getDistance(),r.getLat(),r.getLng(), r.getGrade());
    }
  }
}
