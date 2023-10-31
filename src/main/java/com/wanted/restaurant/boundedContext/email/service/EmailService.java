package com.wanted.restaurant.boundedContext.email.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EmailService {
	@Value("${spring.mail.username}")
	private String ADMIN_ADDRESS;
	private final JavaMailSender mailSender;

	@Async("taskExecutor1") // 비동기
	public void sendEmail(String email, String userName, String tempCode) {
		// 테스트를 위해 @test.com인 이메일은 발송하지 않음
		if (email.endsWith("@test.com")) return;

		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(email);
		message.setFrom(ADMIN_ADDRESS);
		message.setSubject(userName+"님의 임시 인증코드 안내 메일입니다.");
		message.setText("안녕하세요 "+userName+"님의 임시 인증 코드는 [" + tempCode +"] 입니다. \n 최초 로그인 시 인증 코드를 입력해주세요.");
		mailSender.send(message);
	}
}