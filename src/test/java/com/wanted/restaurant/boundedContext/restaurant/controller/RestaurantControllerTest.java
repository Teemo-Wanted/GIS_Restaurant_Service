package com.wanted.restaurant.boundedContext.restaurant.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import com.wanted.restaurant.base.jwt.JwtProvider;
import com.wanted.restaurant.boundedContext.member.entity.Member;
import com.wanted.restaurant.boundedContext.member.repository.MemberRepository;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class RestaurantControllerTest {
	@Autowired
	private MockMvc mvc;

	@Autowired
	private MemberRepository memberRepository;

	@Autowired
	private JwtProvider jwtProvider;

	private Member user1;
	private String user1Token;

	@BeforeEach
	void init() {
		user1 = memberRepository.findByAccount("user1").get();
		user1Token = jwtProvider.genToken(user1.toClaims(), 100);
	}

	@Test
	@DisplayName("GET /restaurant/list 는 해당 지역의 식당 목록을 출력한다. 정렬 기준 미선택시 거리순 정렬")
	void t1() throws Exception {
		// When
		ResultActions resultActions = mvc
			.perform(
				get("/restaurant/list")
					.header("Authorization", "Bearer " + user1Token) // 헤더에 Authorization 값을 추가
					// 경기도 안양시 위, 경도, 범위
					.param("lat", "37.3897")
					.param("lng", "126.9533556")
					.param("range", "1")
			)
			.andDo(print());

		// Then
		resultActions
			.andExpect(status().is2xxSuccessful())
			.andExpect(jsonPath("$.resultCode").value("S-1"))
			.andExpect(jsonPath("$.msg").value("경기안양시의 식당 조회 결과"))
			.andExpect(jsonPath("$.data.restaurants[0].name").value("다께야우동"));
	}

	@Test
	@DisplayName("GET /restaurant/list 는 식당 목록을 평점순으로 반환하나 호출 시점에 따라 데이터가 달라져 존재 여부만 테스트")
	void t2() throws Exception {
		// When
		ResultActions resultActions = mvc
			.perform(
				get("/restaurant/list")
					.header("Authorization", "Bearer " + user1Token) // 헤더에 Authorization 값을 추가
					// 경기도 안양시 위, 경도, 범위
					.param("lat", "37.3897")
					.param("lng", "126.9533556")
					.param("range", "1")
					.param("orderType", "score")
			)
			.andDo(print());

		// Then
		resultActions
			.andExpect(status().is2xxSuccessful())
			.andExpect(jsonPath("$.resultCode").value("S-1"))
			.andExpect(jsonPath("$.msg").value("경기안양시의 식당 조회 결과"))
			.andExpect(jsonPath("$.data.restaurants[0].name").isNotEmpty())
			.andExpect(jsonPath("$.data.restaurants[0].distance").isNotEmpty())
			.andExpect(jsonPath("$.data.restaurants[0].grade").isNotEmpty())
			.andExpect(jsonPath("$.data.restaurants[1].name").isNotEmpty())
			.andExpect(jsonPath("$.data.restaurants[1].distance").isNotEmpty())
			.andExpect(jsonPath("$.data.restaurants[1].grade").isNotEmpty());

	}

	@Test
	@DisplayName("GET /restaurant/{id} 는 해당 식당의 식당 정보와 리뷰를 출력한다.")
	void t3() throws Exception {
		// When
		ResultActions resultActions = mvc
			.perform(
				get("/restaurant/1")
					.header("Authorization", "Bearer " + user1Token) // 헤더에 Authorization 값을 추가
			)
			.andDo(print());

		// Then
		resultActions
			.andExpect(status().is2xxSuccessful())
			.andExpect(jsonPath("$.resultCode").value("S-1"))
			.andExpect(jsonPath("$.msg").value("식당 정보 조회 성공"))
			// API 호출 시점에 따라 식당 정보가 달라지기 때문에 장소가 있고 리뷰 내용이 잘 들어가있는지로 검증하도록 변경
			.andExpect(jsonPath("$.data.businessPlaceName").exists())
			.andExpect(jsonPath("$.data.sigunName").exists())
			.andExpect(jsonPath("$.data.evaluations[0].content").value("맛있어용"));
	}
}