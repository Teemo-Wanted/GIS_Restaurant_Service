package com.wanted.restaurant.base.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.wanted.restaurant.base.interceptor.AuthorizationInterceptor;
import com.wanted.restaurant.base.resolver.LoginUserResolver;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MvcConfig implements WebMvcConfigurer {
  private final AuthorizationInterceptor authorizationInterceptor;
  private final LoginUserResolver loginUserResolver;

  @Value("${discord.webhookURL}")
  private  String discordUrl; // 디스코드 웹훅

  @Value("${open-api.url}")
  private String BASE_URL; // 경기도 공공데이터 포털 open api 호출 url


  @Override
  public void addInterceptors(InterceptorRegistry registry){
    registry.addInterceptor(authorizationInterceptor).excludePathPatterns("/member/signup","/member/signin", "/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html", "/api/event");
  }
  @Override
  public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
    resolvers.add(loginUserResolver);
  }
}
