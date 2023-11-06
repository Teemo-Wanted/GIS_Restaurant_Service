package com.wanted.restaurant.base.resolver;

import org.springframework.stereotype.Component;

import com.wanted.restaurant.boundedContext.member.entity.Member;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class LoginMemberContext {
  private final ThreadLocal<LoginMember> loginMemberThreadLocal = new ThreadLocal<>();
  public void save(Member member){
    loginMemberThreadLocal.set(LoginMember.of(member));
  }
  public LoginMember getLoginMember(){
    return loginMemberThreadLocal.get();
  }
  public void remove(){
    loginMemberThreadLocal.remove();
  }
}
