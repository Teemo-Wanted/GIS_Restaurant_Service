package com.wanted.restaurant.boundedContext.member.dto;

import lombok.Data;

@Data
public class TokenDTO {
	private String accessToken;
	private String refreshToken;

	public TokenDTO (String accessToken, String refreshToken) {
		this.accessToken = accessToken;
		this.refreshToken = refreshToken;
	}
}
