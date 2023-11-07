package com.wanted.restaurant.boundedContext.restaurant.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class RestaurantQuery {

  @AllArgsConstructor
  @NoArgsConstructor
  @Getter
  public static class RestaurantFeed{
    private long id;
    private String name;
    private String type;
    private double lat;
    private double lng;
    private double distance;
  }
}
