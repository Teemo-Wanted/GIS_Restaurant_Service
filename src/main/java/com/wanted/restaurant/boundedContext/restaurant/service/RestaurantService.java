package com.wanted.restaurant.boundedContext.restaurant.service;

import com.wanted.restaurant.base.rsData.RsData;
import com.wanted.restaurant.boundedContext.restaurant.dto.RestaurantQuery;
import com.wanted.restaurant.boundedContext.restaurant.dto.RestaurantQuery.RestaurantFeed;
import com.wanted.restaurant.boundedContext.restaurant.dto.RestaurantQuery.RestaurantFeedInterface;
import com.wanted.restaurant.boundedContext.restaurant.dto.RestaurantResponse;
import com.wanted.restaurant.boundedContext.restaurant.entity.Restaurant;
import com.wanted.restaurant.boundedContext.restaurant.repository.RestaurantRepository;
import com.wanted.restaurant.boundedContext.sigungu.entity.Sigungu;
import com.wanted.restaurant.boundedContext.sigungu.repository.SigunguRepository;
import com.wanted.restaurant.boundedContext.sigungu.service.SigunguService;

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
  private final SigunguService sigunguService;

  public RsData<RestaurantResponse.RestaurantList> search(double lat,double lng,double range,int page,int size){
    RsData<Sigungu> sigunguRsData = sigunguService.get(lat, lng);

    if(sigunguRsData.isFail())
      return RsData.of("F-1", "존재하지 않는 지역입니다.");

    Slice<RestaurantFeedInterface> restaurantsInterfaces = restaurantRepository.searchRestaurants(lat,lng,range,PageRequest.of(page,size));
    Slice<RestaurantFeed> restaurants = toRestaurantFeed(restaurantsInterfaces);
    return RsData.of("S-1", "%s의 식당 조회 결과".formatted(sigunguRsData.getData().getDosi() + sigunguRsData.getData().getSigungu()), RestaurantResponse.RestaurantList.of(restaurants));
  }
  private Slice<RestaurantFeed> toRestaurantFeed(Slice<RestaurantFeedInterface> feedInterfaces){
    List<RestaurantFeedInterface> interfaceList = feedInterfaces.toList();
    List<RestaurantFeed> feeds = interfaceList.stream().map(RestaurantFeed::of).toList();
    return new SliceImpl<>(feeds,feedInterfaces.getPageable(),feedInterfaces.hasNext());
  }

  public List<Restaurant> getAll() {
    return restaurantRepository.findAll();
  }
}
