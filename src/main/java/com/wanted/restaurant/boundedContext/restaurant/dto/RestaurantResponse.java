package com.wanted.restaurant.boundedContext.restaurant.dto;

import com.wanted.restaurant.boundedContext.restaurant.entity.Restaurant;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;

public class RestaurantResponse {
  @AllArgsConstructor
  @NoArgsConstructor
  public static class RestaurantList{
    private int page;
    private int size;
    private boolean hasNext;
    private List<RestaurantListElement> restaurants;
  }

  @AllArgsConstructor
  @NoArgsConstructor
  public static class RestaurantListElement{
    private long id;
    private String name;
    private String type;
    private double distance;
    private double lat;
    private double lng;
  }
}
