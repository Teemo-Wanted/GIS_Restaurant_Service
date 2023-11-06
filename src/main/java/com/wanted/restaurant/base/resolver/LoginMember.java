package com.wanted.restaurant.base.resolver;

import lombok.AllArgsConstructor;
import lombok.Getter;

import com.wanted.restaurant.boundedContext.member.entity.Member;
@Getter
@AllArgsConstructor
public class LoginMember {
  private Long id;
  private String account;
  private String accessToken;
  public static LoginMember of(Member member){
    return new LoginMember(member.getId(),member.getAccount(),member.getAccessToken());
  }
}
