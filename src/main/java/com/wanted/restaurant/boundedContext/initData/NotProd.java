package com.wanted.restaurant.boundedContext.initData;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.wanted.restaurant.boundedContext.member.entity.Member;
import com.wanted.restaurant.boundedContext.member.repository.MemberRepository;
import com.wanted.restaurant.boundedContext.restaurant.entity.Restaurant;
import com.wanted.restaurant.boundedContext.restaurant.repository.RestaurantRepository;
import com.wanted.restaurant.boundedContext.sigungu.service.SigunguService;
import com.wanted.restaurant.util.Ut;

import lombok.RequiredArgsConstructor;

@Configuration
@Profile({"dev", "test"})
@RequiredArgsConstructor
public class NotProd {

	@Bean
	CommandLineRunner initData(MemberRepository memberRepository, SigunguService sigunguService,
		RestaurantRepository restaurantRepository) {

		String password = Ut.encrypt.encryptPW("1234");
		return args -> {
			List<Member> memberList = new ArrayList<>();
			Member user1 = Member.builder()
				.account("user1")
				.password(password)
				.email("user1@test.com")
				// 유효기간 : 1년(2023-11-06)
				.accessToken(
					"eyJhbGciOiJIUzUxMiJ9.eyJib2R5Ijoie1wiaWRcIjoxLFwiYWNjb3VudFwiOlwidXNlcjFcIn0iLCJleHAiOjE3MzA3ODc1NDd9.JFALSiab13HzYvVjrmv5nWG_KAza579-HwifKL_oaO1f6CF7IFJ1kVXrGcKRuM0v1kD4KeTW7KjeMPavmOtOZA")
				.build();

			Member user2 = Member.builder()
				.account("user2")
				.password(password)
				.email("user2@test.com")
				.build();

			Member user3 = Member.builder()
				.account("user3")
				.password(password)
				.tempCode(123456)
				.email("user3@test.com")
				.build();

			memberList.addAll(List.of(user1, user2, user3));
			memberRepository.saveAll(memberList);

			// 시군구 정보 init
			sigunguService.initSigunguData();

			// 식당 정보 초기데이터
			Restaurant restaurant1 = Restaurant.builder()
				.sigunName("고양시") // 시군명
				.businessPlaceName("하오츠가")
				.sanitationBusinessName("중국식")
				.refinedWGS84Latitude(37.6074127788)
				.refinedWGS84Longitude(126.8917434058)
				.build();

			Restaurant restaurant2 = Restaurant.builder()
				.sigunName("고양시") // 시군명
				.businessPlaceName("니하오마라탕 일산점")
				.sanitationBusinessName("중국식")
				.refinedWGS84Latitude(37.6765511227)
				.refinedWGS84Longitude(126.7517649851)
				.build();

			Restaurant restaurant3 = Restaurant.builder()
				.sigunName("고양시") // 시군명
				.businessPlaceName("하오츠가")
				.sanitationBusinessName("중국식")
				.refinedWGS84Latitude(37.6074127788)
				.refinedWGS84Longitude(126.8917434058)
				.build();

			Restaurant restaurant4 = Restaurant.builder()
				.sigunName("고양시") // 시군명
				.businessPlaceName("소림마라고양덕양점")
				.sanitationBusinessName("중국식")
				.refinedWGS84Latitude(37.6275395982)
				.refinedWGS84Longitude(126.8301680093)
				.build();

			Restaurant restaurant5 = Restaurant.builder()
				.sigunName("고양시") // 시군명
				.businessPlaceName("마라영웅")
				.sanitationBusinessName("중국식")
				.refinedWGS84Latitude(37.6867753865)
				.refinedWGS84Longitude(126.7709996335)
				.build();

			restaurantRepository.saveAll(List.of(restaurant1, restaurant2, restaurant3, restaurant4, restaurant5));

		};

	}

}