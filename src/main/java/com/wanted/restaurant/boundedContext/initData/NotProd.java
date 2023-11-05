package com.wanted.restaurant.boundedContext.initData;


import java.util.ArrayList;
import java.util.List;

import com.wanted.restaurant.boundedContext.sigungu.service.SigunguService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.wanted.restaurant.boundedContext.member.entity.Member;
import com.wanted.restaurant.boundedContext.member.repository.MemberRepository;
import com.wanted.restaurant.util.Ut;

@Configuration
@Profile({"dev", "test"})
@RequiredArgsConstructor
public class NotProd {

	private final SigunguService sigunguService;
	@Bean
	CommandLineRunner initData(MemberRepository memberRepository) {

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
		};

	}


}