package com.wanted.restaurant.util.openAPI;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.wanted.restaurant.boundedContext.restaurant.entity.Restaurant;
import com.wanted.restaurant.boundedContext.restaurant.repository.RestaurantRepository;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class OpenAPIPipelineTest {

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private OpenAPIPipeline openAPIPipeline;

    @Test
    @DisplayName("OpenAPI 데이터 파이프라인 작업에 성공한다.")
    void 데이터_파이프라인() throws Exception {
        // When
        openAPIPipeline.pipeline();
        // Then
        List<Restaurant> restaurants = restaurantRepository.findAll();
        assertThat(restaurants.size()).isGreaterThan(0);
    }

}