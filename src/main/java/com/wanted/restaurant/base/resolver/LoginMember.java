package com.wanted.restaurant.base.resolver;

import com.wanted.restaurant.boundedContext.member.entity.Member;

import lombok.AllArgsConstructor;
import lombok.Getter;
@Getter
@AllArgsConstructor
public class LoginMember {
  private Long id;
  private String account;
  public static LoginMember of(Member member){
    return new LoginMember(member.getId(),member.getAccount());
  }
}
