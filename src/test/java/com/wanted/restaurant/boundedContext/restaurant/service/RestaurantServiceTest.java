package com.wanted.restaurant.boundedContext.restaurant.service;

import com.wanted.restaurant.base.rsData.RsData;
import com.wanted.restaurant.boundedContext.restaurant.dto.RestaurantQuery;
import com.wanted.restaurant.boundedContext.restaurant.dto.RestaurantQuery.RestaurantFeed;
import com.wanted.restaurant.boundedContext.restaurant.dto.RestaurantQuery.RestaurantFeedInterface;
import com.wanted.restaurant.boundedContext.restaurant.dto.RestaurantResponse;
import com.wanted.restaurant.boundedContext.restaurant.repository.RestaurantRepository;
import com.wanted.restaurant.boundedContext.sigungu.entity.Sigungu;
import com.wanted.restaurant.boundedContext.sigungu.service.SigunguService;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.SliceImpl;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;

@ExtendWith(MockitoExtension.class)
public class RestaurantServiceTest {
  @InjectMocks
  RestaurantService restaurantService;

  @Mock
  SigunguService sigunguService;
  @Mock
  RestaurantRepository restaurantRepository;

  @Test
  @DisplayName("search 성공 테스트")
  void search_success(){
    //given (mocking & prepare success data)
    List<RestaurantFeedInterface> sampleRestaurants = new ArrayList<>();
    List<RestaurantFeed> expectRestaurantList = new ArrayList<>();
    for(long i=0;i<5;i++){
      // 경기도 안양시 주입
      sampleRestaurants.add(new RestaurantFeedImpl(i,"sample restaurant name "+i,"sample type",
          37.3897,126.9533556,0.1D));
      expectRestaurantList.add(new RestaurantFeed(i,"sample restaurant name "+i,"sample type",
          37.3897,126.9533556,0.1D));
    }
    int samplePage = 0;
    int sampleSize = 5;
    Mockito.when(restaurantRepository.searchRestaurants(anyDouble(),anyDouble(),anyDouble(), any()))
        .thenReturn(new SliceImpl<>(sampleRestaurants,PageRequest.of(samplePage,sampleSize),false));

    Sigungu sigungu = new Sigungu("경기도", "안양시", 37.3897,126.9533556);
    Mockito.when(sigunguService.get(anyDouble(), anyDouble()))
        .thenReturn(RsData.of("S-1", "지역 조회 성공", sigungu));

    RestaurantResponse.RestaurantList expectResult = createExpectResult(expectRestaurantList,samplePage,sampleSize);

    //when (execute method)
    RestaurantResponse.RestaurantList response = restaurantService
        .search(37.3897,126.9533556,12000,0,10).getData();

    //then (verify)
    Assertions.assertThat(response).usingRecursiveComparison().isEqualTo(expectResult);
    Mockito.verify(restaurantRepository,Mockito.times(1))
        .searchRestaurants(anyDouble(),anyDouble(),anyDouble(),any());
  }
  private RestaurantResponse.RestaurantList createExpectResult(List<RestaurantFeed> restaurants,int page,int size){
    List<RestaurantResponse.RestaurantListElement> list = new ArrayList<>();
    for(RestaurantFeed r : restaurants){
      list.add(new RestaurantResponse.RestaurantListElement(r.getId(),r.getName(),r.getType(),
              0.1D,r.getLat(),r.getLng()));
    }
    return new RestaurantResponse.RestaurantList(page,size,false,list);
  }
  @NoArgsConstructor
  private static class RestaurantFeedImpl implements RestaurantFeedInterface {
    private long id;
    private String name;
    private String type;
    private double lat;
    private double lng;
    private double distance;

    @Override
    public Long getId() {
      return this.id;
    }

    @Override
    public String getName() {
      return this.name;
    }

    @Override
    public String getType() {
      return this.type;
    }

    @Override
    public Double getLat() {
      return this.lat;
    }

    @Override
    public Double getLng() {
      return this.lng;
    }

    @Override
    public Double getDistance(){
      return this.distance;
    }
    public RestaurantFeedImpl(long id,String name,String type,double lat,double lng, double distance){
      this.id=id;
      this.name=name;
      this.type=type;
      this.lat=lat;
      this.lng=lng;
      this.distance=distance;
    }
  }
}
