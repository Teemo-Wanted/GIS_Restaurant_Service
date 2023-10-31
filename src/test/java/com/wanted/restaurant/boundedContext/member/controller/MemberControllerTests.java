package com.wanted.restaurant.boundedContext.member.controller;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wanted.restaurant.base.rsData.RsData;
import com.wanted.restaurant.boundedContext.member.entity.Member;
import com.wanted.restaurant.boundedContext.member.repository.MemberRepository;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class MemberControllerTests {
	@Autowired
	private MockMvc mvc;

	@Autowired
	private MemberRepository memberRepository;

	@Test
	@DisplayName("POST /member/signup 은 회원가입을 처리한다.")
	void t1() throws Exception {
		// When
		ResultActions resultActions = mvc
			.perform(
				post("/member/signup")
					.content("""
						{
						    "account": "puar12",
						    "password": "cjfgus2514",
						    "email": "r4560798@test.com"
						}
						""".stripIndent())
					// JSON 형태로 보내겠다
					.contentType(new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8))
			)
			.andDo(print());

		// Then
		resultActions
			.andExpect(status().is2xxSuccessful())
			.andExpect(jsonPath("$.resultCode").value("S-1"))
			.andExpect(jsonPath("$.msg").value("회원가입 완료 최초 로그인 시 이메일 인증 코드를 확인하고 입력해주세요"));

		// 인증 코드 값이 들어있는지 확인
		Member member = memberRepository.findByAccount("puar12").get();
		assertThat(member.getTempCode()).isNotNull();
	}

	@Test
	@DisplayName("회원가입 시 이메일을 입력하지 않으면 가입이 되지 않는다.")
	void t2() throws Exception {
		// When
		ResultActions resultActions = mvc
			.perform(
				post("/member/signup")
					.content("""
						{
						    "account": "puar12",
						    "password": "cjfgus2514",
						    "email": ""
						}
						""".stripIndent())
					// JSON 형태로 보내겠다
					.contentType(new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8))
			)
			.andDo(print());

		// Then
		resultActions
			.andExpect(status().is2xxSuccessful())
			.andExpect(jsonPath("$.resultCode").value("F-1"))
			.andExpect(jsonPath("$.msg").value("이메일을 입력해주세요"));
	}

	@Test
	@DisplayName("회원가입 시 비밀번호 제약조건을 지키지 않으면 가입이 되지 않는다.")
	void t3() throws Exception {
		// When
		ResultActions resultActions = mvc
			.perform(
				post("/member/signup")
					.content("""
						{
						    "account": "puar12",
						    "password": "1234",
						    "email": "aaaaa@test.com"
						}
						""".stripIndent())
					// JSON 형태로 보내겠다
					.contentType(new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8))
			)
			.andDo(print());

		// Then
		resultActions
			.andExpect(status().is2xxSuccessful())
			.andExpect(jsonPath("$.resultCode").value("F-1"))
			.andExpect(jsonPath("$.msg").value("비밀번호는 10자 이상 입력해야 하며, 숫자로만 이루어 질 수 없으며 3회 이상 연속되는 문자를 사용할 수 없습니다."));
	}

	@Test
	@DisplayName("로그인 시 JWT 발급 테스트")
	void t4() throws Exception {
		// When
		ResultActions resultActions = mvc
			.perform(
				post("/member/signin")
					.content("""
                    {
                        "account": "user1",
                        "password": "1234"
                    }
                    """.stripIndent())
					// JSON 형태로 보내겠다
					.contentType(new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8))
			)
			.andDo(print());

		// Then
		resultActions
			.andExpect(status().is2xxSuccessful())
			.andExpect(jsonPath("$.resultCode").value("S-1"))
			.andExpect(jsonPath("$.msg").value( "JWT 발급 성공"))
			.andExpect(jsonPath("$.data").isNotEmpty());
	}

	@Test
	@DisplayName("여러번 로그인 해도 아직 토큰이 유효하다면 동일한 토큰을 발급한다.")
	void t5() throws Exception {
		// When
		ResultActions resultActions = mvc
			.perform(
				post("/member/signin")
					.content("""
                    {
                        "account": "user1",
                        "password": "1234"
                    }
                    """.stripIndent())
					// JSON 형태로 보내겠다
					.contentType(new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8))
			)
			.andDo(print());

		// Then
		resultActions
			.andExpect(status().is2xxSuccessful())
			.andExpect(jsonPath("$.resultCode").value("S-1"))
			.andExpect(jsonPath("$.msg").value( "JWT 발급 성공"));

		MvcResult result = resultActions.andReturn();
		String jsonResponse = result.getResponse().getContentAsString();

		// JSON 문자열을 Java 객체로 변환
		ObjectMapper objectMapper = new ObjectMapper();
		RsData<String> response = objectMapper.readValue(jsonResponse, RsData.class);

		String tmp1 = response.getData();

		// 두 번째 요청 수행
		ResultActions resultActions2 = mvc
			.perform(
				post("/member/signin")
					.content("""
                    {
                        "account": "user1",
                        "password": "1234"
                    }
                    """.stripIndent())
					// JSON 형태로 보내겠다
					.contentType(new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8))
			)
			.andDo(print());

		// Then (두 번째 응답 처리)
		resultActions2
			.andExpect(status().is2xxSuccessful())
			.andExpect(jsonPath("$.resultCode").value("S-1"))
			.andExpect(jsonPath("$.msg").value("JWT 발급 성공"));

		MvcResult result2 = resultActions2.andReturn();
		String jsonResponse2 = result2.getResponse().getContentAsString();

		// 두 번째 응답 데이터를 Java 객체로 변환
		RsData<String> response2 = objectMapper.readValue(jsonResponse2, RsData.class);

		String tmp2 = response2.getData();

		assertThat(tmp1).isEqualTo(tmp2);
	}

	@Test
	@DisplayName("올바른 이메일 인증 코드를 입력했을 때 JWT가 발급되어야 함")
	void t6() throws Exception {
		// When
		ResultActions resultActions = mvc
			.perform(
				post("/member/signin")
					.content("""
                    {
                        "account": "user3",
                        "password": "1234",
                        "tempCode" : "123456"
                    }
                    """.stripIndent())
					// JSON 형태로 보내겠다
					.contentType(new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8))
			)
			.andDo(print());

		// Then
		resultActions
			.andExpect(status().is2xxSuccessful())
			.andExpect(jsonPath("$.resultCode").value("S-1"))
			.andExpect(jsonPath("$.msg").value( "JWT 발급 성공"))
			.andExpect(jsonPath("$.data").isNotEmpty());
	}

	@Test
	@DisplayName("잘못된 이메일 인증 코드를 입력했을 때 실패 메시지 출력")
	void t7() throws Exception {
		// When
		ResultActions resultActions = mvc
			.perform(
				post("/member/signin")
					.content("""
                    {
                        "account": "user3",
                        "password": "1234",
                        "tempCode" : "12356"
                    }
                    """.stripIndent())
					// JSON 형태로 보내겠다
					.contentType(new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8))
			)
			.andDo(print());

		// Then
		resultActions
			.andExpect(status().is2xxSuccessful())
			.andExpect(jsonPath("$.resultCode").value("F-1"))
			.andExpect(jsonPath("$.msg").value( "이메일 인증 코드가 일치하지 않습니다."))
			.andExpect(jsonPath("$.data").isEmpty());
	}
}
