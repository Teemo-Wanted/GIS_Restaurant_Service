package com.wanted.restaurant.boundedContext.initData;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.wanted.restaurant.boundedContext.evalutation.dto.EvaluateRequestDto;
import com.wanted.restaurant.boundedContext.evalutation.service.EvaluationService;
import com.wanted.restaurant.boundedContext.member.entity.Member;
import com.wanted.restaurant.boundedContext.member.repository.MemberRepository;
import com.wanted.restaurant.boundedContext.sigungu.service.SigunguService;
import com.wanted.restaurant.util.Ut;
import com.wanted.restaurant.util.openAPI.OpenAPIPipeline;

import lombok.RequiredArgsConstructor;

@Configuration
@Profile({"dev", "test"})
@RequiredArgsConstructor
public class NotProd {

	@Bean
	CommandLineRunner initData(MemberRepository memberRepository, SigunguService sigunguService,
		OpenAPIPipeline openAPIPipeline, EvaluationService evaluationService) {

		String password = Ut.encrypt.encryptPW("1234");
		return args -> {
			List<Member> memberList = new ArrayList<>();
			Member user1 = Member.builder()
				.account("user1")
				.password(password)
				.email("user1@test.com")
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

			// 데이터 받아오기
			openAPIPipeline.pipeline();

			EvaluateRequestDto evaluateRequestDto = new EvaluateRequestDto();

			evaluateRequestDto.setContent("맛있어용");
			evaluateRequestDto.setScore(5);
			evaluateRequestDto.setMemberId(1);
			evaluateRequestDto.setRestaurantId(1);

			evaluationService.evaluate(evaluateRequestDto);

			EvaluateRequestDto evaluateRequestDto1 = new EvaluateRequestDto();
			evaluateRequestDto1.setContent("테스트 리뷰" + 1);
			evaluateRequestDto1.setScore(5);
			evaluateRequestDto1.setMemberId(1);
			evaluateRequestDto1.setRestaurantId(209);
			evaluationService.evaluate(evaluateRequestDto1);

			EvaluateRequestDto evaluateRequestDto2 = new EvaluateRequestDto();
			evaluateRequestDto2.setContent("테스트 리뷰" + 2);
			evaluateRequestDto2.setScore(4);
			evaluateRequestDto2.setMemberId(1);
			evaluateRequestDto2.setRestaurantId(156);

			evaluationService.evaluate(evaluateRequestDto2);
		};
	}

}