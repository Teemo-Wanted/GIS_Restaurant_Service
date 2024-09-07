package com.wanted.restaurant.boundedContext.member.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wanted.restaurant.base.jwt.JwtProvider;
import com.wanted.restaurant.base.rsData.RsData;
import com.wanted.restaurant.boundedContext.email.service.EmailService;
import com.wanted.restaurant.boundedContext.member.dto.TokenDTO;
import com.wanted.restaurant.boundedContext.member.entity.AlarmType;
import com.wanted.restaurant.boundedContext.member.entity.Member;
import com.wanted.restaurant.boundedContext.member.repository.MemberRepository;
import com.wanted.restaurant.boundedContext.sigungu.service.SigunguService;
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

	private final SigunguService sigunguService;

	private MemberService memberService;

	private final ApplicationContext applicationContext;

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
	public RsData<TokenDTO> login(String account, String password, String tempCode) {
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

		String accessToken = genAccessToken(member);
		String refreshToken = genRefreshToken(member);

		// 리프레시 토큰 갱신
		member.updateRefreshToken(refreshToken);
		_updateRefreshToken__Cached(member); // Cache 값 갱신

		return RsData.of("S-1", "토큰 발급 성공", new TokenDTO(accessToken, refreshToken));
	}

	// 캐시된 리프레시 토큰 있다면 해당 메서드 실행하지 않음
	@Cacheable(value = "Refresh", key = "#targetId")
	public String getRefreshTokenByCached(long targetId) {
		if (memberService == null) {
			applicationContext.getBean("memberService", MemberService.class);
		}

		Member member = memberService.get(targetId).getData();
		return member.getRefreshToken();
	}

	private String _updateRefreshToken__Cached(Member member) {
		if (memberService == null) {
			// 의존성 순환 참조 때문에 RedisService를 의존성 주입 불가
			// 따라서 Context에 등록된 memberService 객체 가져와서 실행
			memberService = applicationContext.getBean("memberService", MemberService.class);
		}

		return memberService.updateRefreshToken__Cached(member);
	}

	// 캐시된 리프레시 토큰을 갱신
	@CachePut(value = "Refresh", key = "#member.id")
	public String updateRefreshToken__Cached(Member member) {
		return member.getRefreshToken();
	}

	@CacheEvict(value = "Refresh", key = "#memberId")
	@Transactional
	public RsData deleteRefreshToken(Long memberId) {

		Member member = memberRepository.findById(memberId).orElse(null);
		if(member == null) {
			return RsData.of("F-1", "사용자 Id : " + memberId + " 는 존재하지 않는 회원입니다.");
		}
		// DB 초기화
		member.resetRefreshToken();

		return RsData.of("S-1", "사용자 Id : " + member.getAccount() + "의 Refresh Token 초기화 성공 재로그인 해주세요", null);
	}

	private String genRefreshToken(Member member) {
		Map<String, Object> memberInfo = member.toClaims();
		memberInfo.put("Type", "Refresh");
		// 3일짜리 RT 발급
		return jwtProvider.genToken(member.toClaims(), 60 * 60 * 24 * 3);
	}

	private String genAccessToken(Member member) {
		// 1일 짜리 JWT 발급
		return jwtProvider.genToken(member.toClaims(), 60 * 60 * 24 * 1);
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

	@Transactional
	public RsData update(Long memberId, String doSi, String sgg, String alarm) {
		Member member = memberRepository.findById(memberId).get();

		// 강원 or 강원도, 세종 or 세종시 or 세종특별시 등 입력 다양하게 할 수 있으니 앞에 2글자만 추출
		RsData<List<Double>> rsDataLatAndLon = sigunguService.getLatAndLonByDoSiAndSgg(doSi.substring(0, 2),
			sgg.substring(0, 2));

		if (rsDataLatAndLon.isFail())
			return rsDataLatAndLon;

		AlarmType alarmType;
		if (alarm.contains("y") || alarm.contains("Y"))
			alarmType = AlarmType.YES;
		else
			alarmType = AlarmType.NO;

		List<Double> data = rsDataLatAndLon.getData();

		Member modifyMember = member.toBuilder()
			.lon(String.valueOf(data.get(0)))
			.lat(String.valueOf(data.get(1)))
			.alarmType(alarmType)
			.build();

		memberRepository.save(modifyMember);

		return RsData.of("S-1", "회원 정보 업데이트 성공", modifyMember);
	}

	public RsData<Member> get(Long memberId) {
		Member member = memberRepository.findById(memberId).get();
		if (member == null)
			return RsData.of("F-1", "일치하는 회원정보가 없습니다.");
		return RsData.of("S-1", "회원 조회 성공", member);
	}

	public List<Member> getAllMembersByAlarmYes() {
		return memberRepository.findAllByAlarmType(AlarmType.YES);
	}

	public Member createByClaims(Map<String, Object> claims) {
		return Member.builder()
			.id((long)(int)claims.get("id"))
			.account((String)claims.get("account"))
			.build();
	}
}
