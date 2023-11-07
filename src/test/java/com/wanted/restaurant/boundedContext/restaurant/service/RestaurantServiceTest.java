package com.wanted.restaurant.boundedContext.restaurant.service;

import com.wanted.restaurant.boundedContext.restaurant.dto.RestaurantQuery.RestaurantFeed;
import com.wanted.restaurant.boundedContext.restaurant.dto.RestaurantResponse;
import com.wanted.restaurant.boundedContext.restaurant.repository.RestaurantRepository;
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
  RestaurantRepository restaurantRepository;

  @Test
  @DisplayName("search 성공 테스트")
  void search_success(){
    //given (mocking & prepare success data)
    List<RestaurantFeed> sampleRestaurants = new ArrayList<>();
    for(long i=0;i<5;i++){
      sampleRestaurants.add(new RestaurantFeed(i,"sample restaurant name "+i,"sample type",
              37.655225D,127.515502D,0.1D));
    }
    int samplePage = 0;
    int sampleSize = 5;
    Mockito.when(restaurantRepository.searchRestaurants(anyDouble(),anyDouble(),anyDouble(), any()))
            .thenReturn(new SliceImpl<>(sampleRestaurants,PageRequest.of(samplePage,sampleSize),false));

    RestaurantResponse.RestaurantList expectResult = createExpectResult(sampleRestaurants,samplePage,sampleSize);

    //when (execute method)
    RestaurantResponse.RestaurantList response = restaurantService
            .search(37.655225D,127.515502D,12000,0,10);

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
}
