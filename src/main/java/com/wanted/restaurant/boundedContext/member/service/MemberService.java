package com.wanted.restaurant.boundedContext.member.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wanted.restaurant.base.jwt.JwtProvider;
import com.wanted.restaurant.base.rsData.RsData;
import com.wanted.restaurant.boundedContext.email.service.EmailService;
import com.wanted.restaurant.boundedContext.member.entity.Member;
import com.wanted.restaurant.boundedContext.member.repository.MemberRepository;
import com.wanted.restaurant.util.Ut;

import at.favre.lib.crypto.bcrypt.BCrypt;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {
	private final MemberRepository memberRepository;
	private final JwtProvider jwtProvider;

	private final EmailService emailService;

	@Transactional
	public RsData join(String account, String password, String email) {
		Optional<Member> opMember = memberRepository.findByAccount(account);
		if (opMember.isPresent()) {
			return RsData.of("F-1", "이미 가입한 Id가 있습니다.");
		}

		// 인증코드 6자리 발급 및 저장
		int temp = createNumber();

		Member member = Member.builder()
			.account(account)
			.password(Ut.encrypt.encryptPW(password))
			.email(email)
			.tempCode(temp)
			.build();

		memberRepository.save(member);

		// 비동기 방식의 메서드를 다른 class에서 호출해야 하기에 따로 호출
		emailService.sendEmail(email, account, Integer.toString(temp));

		return RsData.of("S-1", "회원가입 완료 최초 로그인 시 이메일 인증 코드를 확인하고 입력해주세요");
	}

	public int createNumber() {
		return (int)(Math.random() * (90000)) + 100000;// (int) Math.random() * (최댓값-최소값+1) + 최소값
	}

	@Transactional
	public RsData<String> login(String account, String password, String tempCode) {
		Member member = getMemberByAccount(account);
		// 가입된 ID가 없는 경우
		if (member == null) {
			return RsData.of("F-1", "일치하는 ID가 없습니다. 회원가입 후 이용해주세요");
		}

		BCrypt.Result result = verifyPassword(password, member.getPassword());
		// 비밀번호 일치하지 않은 경우
		if (!result.verified) {
			return RsData.of("F-1", "PW가 일치하지 않습니다.");
		}
		// 이메일 인증이 필요한 경우
		if (isEmailVerificationRequired(member)) {
			// 인증이 필요한데 코드를 입력 안한 경우
			if (tempCode.isBlank()) {
				return RsData.of("F-1", "이메일 인증이 필요합니다. 코드를 입력하세요");
			}
			// 인증 코드가 일치하지 않는 경우
			if (!member.getTempCode().equals(Integer.parseInt(tempCode))) {
				return RsData.of("F-1", "이메일 인증 코드가 일치하지 않습니다.");
			}
			// 일치하면 인증 처리
			updateMemberAfterEmailVerification(member);
		}

		String accessToken = member.getAccessToken();
		// 토큰 갱신이 필요한 경우
		if (isAccessTokenRenewalRequired(accessToken)) {
			String newAccessToken = renewAccessToken(member);
			return RsData.of("S-1", "JWT 발급 성공", newAccessToken);
		}

		return RsData.of("S-1", "JWT 발급 성공", accessToken);
	}

	private Member getMemberByAccount(String account) {
		return memberRepository.findByAccount(account).orElse(null);
	}

	private BCrypt.Result verifyPassword(String password, String hashedPassword) {
		return Ut.encrypt.verifyPW(password, hashedPassword);
	}

	private boolean isEmailVerificationRequired(Member member) {
		return member.getTempCode() != null;
	}

	private void updateMemberAfterEmailVerification(Member member) {
		Member modifiedMember = member.toBuilder().tempCode(null).build();
		memberRepository.save(modifiedMember);
	}

	private boolean isAccessTokenRenewalRequired(String accessToken) {
		return accessToken == null || !jwtProvider.verify(accessToken);
	}

	private String renewAccessToken(Member member) {
		Map<String, Object> claims = new HashMap<>();
		claims.put("id", member.getId());
		claims.put("account", member.getAccount());

		String newAccessToken = jwtProvider.genToken(claims, 60 * 60 * 24 * 365);

		Member modifiedMember = member.toBuilder().accessToken(newAccessToken).build();
		memberRepository.save(modifiedMember);

		return newAccessToken;
	}
}
