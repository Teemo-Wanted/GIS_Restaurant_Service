package com.wanted.restaurant.base.jwt;
import java.util.Base64;
import java.util.Date;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.wanted.restaurant.util.Ut;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtProvider {
	private SecretKey cachedSecretKey;

	@Value("${custom.jwt.secretKey}")
	private String secretKeyPlain;

	private SecretKey _getSecretKey() {
		// 시크릿 키 객체 생성 : 시크릿 키 평문 Base64 인코딩 -> hmac 인코딩
		String keyBase64Encoded = Base64.getEncoder().encodeToString(secretKeyPlain.getBytes());
		return Keys.hmacShaKeyFor(keyBase64Encoded.getBytes());
	}

	// 시크릿키 객체가 있으면 저장된거 사용, 없으면 생성
	public SecretKey getSecretKey() {
		if (cachedSecretKey == null) cachedSecretKey = _getSecretKey();

		return cachedSecretKey;
	}

	// 토큰에 들어갈 정보와 만료 시간을 받으면 토큰을 생성하는 메서드
	public String genToken(Map<String, Object> claims, int seconds) {
		long now = new Date().getTime();
		Date accessTokenExpiresIn = new Date(now + 1000L * seconds);

		return Jwts.builder()
			.claim("body", Ut.json.toStr(claims))
			.setExpiration(accessTokenExpiresIn)
			// 추후 알고리즘과 비밀키를 사용하여 서버가 서명했는지 검증함
			.signWith(getSecretKey(), SignatureAlgorithm.HS512)
			.compact();
	}

	// 토큰이 유효한지 검사하는 메서드
	public boolean verify(String token) {
		try {
			Jwts.parserBuilder()
				.setSigningKey(getSecretKey())
				.build()
				.parseClaimsJws(token);
		} catch (Exception e) {
			return false;
		}

		return true;
	}

	// 토큰으로부터 정보를 추출하는 메서드
	public Map<String, Object> getClaims(String token) {
		String body = Jwts.parserBuilder()
			.setSigningKey(getSecretKey())
			.build()
			.parseClaimsJws(token)
			.getBody()
			.get("body", String.class);

		return Ut.json.toMap(body);
	}
}