package com.wanted.restaurant.boundedContext.member.entity;

import static jakarta.persistence.GenerationType.*;

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
	private String accessToken;
	@JsonIgnore
	private Integer tempCode;

	private String lat;	 // 위도
	private String lon;  // 경도
	@Enumerated(EnumType.STRING)
	private AlarmType alarmType;
}
