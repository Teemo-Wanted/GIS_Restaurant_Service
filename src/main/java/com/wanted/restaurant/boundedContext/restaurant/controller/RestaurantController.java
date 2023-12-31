package com.wanted.restaurant.boundedContext.restaurant.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wanted.restaurant.base.rsData.RsData;
import com.wanted.restaurant.boundedContext.restaurant.dto.RestaurantDetailDTO;
import com.wanted.restaurant.boundedContext.restaurant.dto.RestaurantResponse;
import com.wanted.restaurant.boundedContext.restaurant.service.RestaurantService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/restaurant" )
@RequiredArgsConstructor
@Tag(name = "RestaurantController", description = "식당 조회 컨트롤러")
public class RestaurantController {

	private final RestaurantService restaurantService;

	@GetMapping("/list")
	@Operation(summary = "지역 위, 경도를 입력받아 식당 조회", security = @SecurityRequirement(name = "bearerAuth"))
	public RsData<RestaurantResponse.RestaurantList> searchList
		(@RequestParam(value = "lat") Double lat, @RequestParam(value = "lng") Double lng,
			@RequestParam(value = "range") Double range,
			@RequestParam(value = "orderType", required = false, defaultValue = "") String orderType,
			@RequestParam(value = "page", required = false, defaultValue = "0") int page, @RequestParam(value = "size", required = false, defaultValue = "10") int size) {

		RsData<RestaurantResponse.RestaurantList> searchRsData = restaurantService.search(lat, lng, range * 1000, orderType, page,
			size);

		return searchRsData;
	}

	@GetMapping("/{id}")
	@Operation(summary = "식당 상세정보 조회", security = @SecurityRequirement(name = "bearerAuth"))
	public RsData<RestaurantDetailDTO> detail(@PathVariable Long id) {

		RsData<RestaurantDetailDTO> detailDTORsData = restaurantService.get(id);

		return detailDTORsData;
	}
}
