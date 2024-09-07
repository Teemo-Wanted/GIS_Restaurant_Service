package com.wanted.restaurant.boundedContext.member.entity;

import static jakarta.persistence.GenerationType.*;

import java.util.HashMap;
import java.util.Map;

import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@DynamicUpdate
public class Member {
	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Long id;
	@Column(unique = true)
	private String account;
	@JsonIgnore
	private String password;
	@Email
	private String email;
	@JsonIgnore
	private String refreshToken;
	@JsonIgnore
	private Integer tempCode;

	private String lat;	 // 위도
	private String lon;  // 경도
	@Enumerated(EnumType.STRING)
	private AlarmType alarmType;

	public Map<String, Object> toClaims() {
		Map<String, Object> result = new HashMap<>();
		result.put("id", getId());
		result.put("account", getAccount());
		return result;
	}

	public void updateRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public void resetRefreshToken() {
		this.refreshToken = null;
	}
}
