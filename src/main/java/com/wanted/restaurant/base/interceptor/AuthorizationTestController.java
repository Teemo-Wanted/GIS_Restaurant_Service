package com.wanted.restaurant.base.interceptor;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wanted.restaurant.base.resolver.LoginMember;
import com.wanted.restaurant.base.resolver.LoginUser;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/v1/authorization/test")
@Tag(name = "TestController", description = "현재 로그인한 사용자 정보 추출 어노테이션 테스트용 API")
public class AuthorizationTestController {
  @GetMapping("")
  @Operation(summary = "현재 로그인한 사용자의 account를 반환한다.")
  public String authorizationTest(@LoginUser LoginMember loginMember){
    return loginMember.getAccount();
  }
}
