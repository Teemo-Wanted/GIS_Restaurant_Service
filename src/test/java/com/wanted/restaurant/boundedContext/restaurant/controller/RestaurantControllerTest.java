package com.wanted.restaurant.boundedContext.restaurant.controller;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

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

	private Member user1;
	private String user1Token;

	@BeforeEach
	void init() {
		// 테스트용 user1 토큰 가져오기
		user1 = memberRepository.findByAccount("user1").get();
		user1Token = user1.getAccessToken();
	}

	@Test
	@DisplayName("GET /restaurant/list 는 해당 지역의 식당을 검색한다. 경기도 안양시 테스트")
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
	@DisplayName("GET /restaurant/{id} 는 해당 식당의 식당 정보와 리뷰를 출력한다.")
	void t2() throws Exception {
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
			.andExpect(jsonPath("$.data.businessPlaceName").value("쌍홍루"))
			.andExpect(jsonPath("$.data.sigunName").value("의정부시"))
			.andExpect(jsonPath("$.data.evaluations[0].content").value("맛있어용"));
	}
}