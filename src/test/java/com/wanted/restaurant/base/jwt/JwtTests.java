package com.wanted.restaurant.base.jwt;

import static org.assertj.core.api.Assertions.*;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import io.jsonwebtoken.security.Keys;
import jakarta.transaction.Transactional;

@ExtendWith(MockitoExtension.class)
class JwtTests {
	private String secretKeyPlain = "secretKeyTestPlainsecretKeyTestPlainsecretKeyTestPlainsecretKeyTestPlain";

	@InjectMocks // 테스트 환경에서 동작할 진짜 객체 생성(메서드 동작 o)
	private JwtProvider jwtProvider;

	/*
		속성값에 테스트용 SecretKey 넣어주기
		- ReflectionTestUtils : 리플렉션을 사용하여 테스트 케이스에 필요한 값을 주입하거나 가져옴
		  - 리플렉션은 일반적으로 private 필드나 메서드 접근할 수 없지만 테스트 환경에서는 가능
		  - setFiled : 주어진 객체의 필드 값 설정
		  - getFiled : 주어진 객체의 필드 값 가져옴
		  - invokeMethod : 주어진 객체의 메서드 호출
	 */
	@BeforeEach
	public void setUp() {
		// secret key 값을 넣어줌
		ReflectionTestUtils.setField(jwtProvider, "secretKeyPlain", secretKeyPlain);
	}

	@Test
	@DisplayName("secretKey 키가 존재해야한다.")
	void t1() {
		assertThat(secretKeyPlain).isNotNull();
	}

	@Test
	@DisplayName("sercretKey 원문으로 hmac 암호화 알고리즘에 맞는 SecretKey 객체를 만들 수 있다.")
	void t2() {
		// 키를 Base64 인코딩 한다.
		String keyBase64Encoded = Base64.getEncoder().encodeToString(secretKeyPlain.getBytes());
		// Base64 인코딩된 키를 이용하여 SecretKey 객체를 만든다.
		SecretKey secretKey = Keys.hmacShaKeyFor(keyBase64Encoded.getBytes());

		assertThat(secretKey).isNotNull();
	}

	@Test
	@DisplayName("JwtProvider 객체로 SecretKey 객체를 생성할 수 있다.")
	void t3() {
		SecretKey secretKey = jwtProvider.getSecretKey();

		assertThat(secretKey).isNotNull();
	}

	@Test
	@DisplayName("SecretKey 객체는 단 한번만 생성되어야 한다.")
	void t4() {
		SecretKey secretKey1 = jwtProvider.getSecretKey();
		SecretKey secretKey2 = jwtProvider.getSecretKey();

		assertThat(secretKey1 == secretKey2).isTrue();
	}

	@Test
	@DisplayName("accessToken 을 얻는다.")
	void t5() {
		Map<String, Object> claims = new HashMap<>();
		claims.put("id", 1L);
		claims.put("username", "admin");

		// 지금으로부터 5시간의 유효기간을 가지는 토큰을 생성
		String accessToken = jwtProvider.genToken(claims, 60 * 60 * 5);

		System.out.println("accessToken : " + accessToken);

		assertThat(accessToken).isNotNull();
	}

	@Test
	@DisplayName("accessToken 을 통해서 claims 를 얻을 수 있다.")
	void t6() {
		Map<String, Object> claims = new HashMap<>();
		claims.put("id", 1L);
		claims.put("username", "admin");

		// 지금으로부터 5시간의 유효기간을 가지는 토큰을 생성
		String accessToken = jwtProvider.genToken(claims, 60 * 60 * 5);

		System.out.println("accessToken : " + accessToken);

		assertThat(jwtProvider.verify(accessToken)).isTrue();

		Map<String, Object> claimsFromToken = jwtProvider.getClaims(accessToken);
		System.out.println("claimsFromToken : " + claimsFromToken);
	}

	@Test
	@DisplayName("만료된 토큰을 유효하지 않다.")
	void t7() {
		Map<String, Object> claims = new HashMap<>();
		claims.put("id", 1L);
		claims.put("username", "admin");

		// 만료 기간을 현재 시간보다 -1초로 설정 -> 만료 토큰 생성
		String accessToken = jwtProvider.genToken(claims, -1);

		System.out.println("accessToken : " + accessToken);

		assertThat(jwtProvider.verify(accessToken)).isFalse();
	}
}