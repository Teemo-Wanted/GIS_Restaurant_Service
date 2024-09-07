package com.wanted.restaurant.base.interceptor;

import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.wanted.restaurant.base.exceptionHandler.AuthenticationException;
import com.wanted.restaurant.base.jwt.JwtProvider;
import com.wanted.restaurant.base.resolver.LoginMemberContext;
import com.wanted.restaurant.boundedContext.member.entity.Member;
import com.wanted.restaurant.boundedContext.member.repository.MemberRepository;
import com.wanted.restaurant.boundedContext.member.service.MemberService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AuthorizationInterceptor implements HandlerInterceptor {
	private final JwtProvider jwtProvider;
	private final MemberService memberService;
	private final LoginMemberContext loginMemberContext;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws
		Exception {
		String bearerToken = request.getHeader("Authorization");
		String refreshToken = request.getHeader("RefreshToken");

		if (bearerToken == null && refreshToken == null) {
			throw new javax.security.sasl.AuthenticationException("AT와 RT 둘 중 하나는 헤더에 포함시켜주세요");
		}
		// AT가 유효하다면 인증처리
		if (bearerToken != null && isTokenValid(bearerToken.substring("Bearer ".length()))) {
			Member member = getMemberFromToken(bearerToken.substring("Bearer ".length()));
			registerLoginMemberContext(Optional.of(member));
			return true;
		}
		// AT유효하지 않은데, RT가 없다면
		if (refreshToken == null) {
			throw new AuthenticationException("만료된 AT입니다. RT를 포함시켜주세요.");
		}
		// RT 유효한지 검사
		if (isTokenValid(refreshToken)) {
			// 사용자 추출
			Map<String, Object> claims = jwtProvider.getClaims(refreshToken);
			long targetId = (long)(int)claims.get("id");
			// 캐시된 RT를 가져옴 (사용자 id 넘겨서)
			String refreshTokenByCache = memberService.getRefreshTokenByCached(targetId);
			// 사용자에게 저장된 RT와 같다면(캐시된 RT와 같은지 비교)
			if (refreshTokenByCache.equals(refreshToken)) {
				// 토큰으로부터 추출한 데이터로 Member를 생성
				Member member = memberService.createByClaims(claims);
				// 1일짜리 AT 재발생해서 반환
				Map<String, Object> newMemberClaims = member.toClaims();
				String newAccessToken = jwtProvider.genToken(newMemberClaims, 60 * 60 * 24 * 1);
				response.addHeader("Authorization", "Bearer " + newAccessToken);
				// 인증 처리
				registerLoginMemberContext(Optional.of(member));
			}
			// 다르다면 변경된 RT이므로 재 로그인
			else {
				throw new AuthenticationException("유효하지 않은 RT입니다. 재 로그인 해주세요.");
			}
		} else {
			throw new AuthenticationException("토큰이 만료되었습니다. 재 로그인 해주세요.");
		}
		return HandlerInterceptor.super.preHandle(request, response, handler);
	}

	private Member getMemberFromToken(String token){
		Map<String, Object> claims = jwtProvider.getClaims(token);
		Member member = memberService.createByClaims(claims);
		return member;
	}


	private boolean isTokenValid(String token) {
		return jwtProvider.verify(token);
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
		Exception ex) throws Exception {
		releaseLoginMemberContext();
		HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
	}

	private void registerLoginMemberContext(Optional<Member> memberOptional) {
		if (memberOptional.isEmpty())
			return;
		loginMemberContext.save(memberOptional.get());
	}

	private void releaseLoginMemberContext() {
		loginMemberContext.remove();
	}
}
