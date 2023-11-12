package com.wanted.restaurant.base.interceptor;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wanted.restaurant.base.resolver.LoginMember;
import com.wanted.restaurant.base.resolver.LoginUser;

@RestController
@RequestMapping("/v1/authorization/test")
public class AuthorizationTestController {
  @GetMapping
  public String authorizationTest(@LoginUser LoginMember loginMember){
    return loginMember.getAccount();
  }
}
