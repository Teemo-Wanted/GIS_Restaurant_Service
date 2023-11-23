package com.wanted.restaurant.boundedContext.discord;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/event")
@Tag(name = "TestController", description = "테스트용 API 컨트롤러")
public class EventController {

	private final WebHookService webHookService;

	@GetMapping("")
	@Operation(summary = "디스코드 웹훅 이벤트 발생 테스트 컨트롤러")
	public String postEvent() {
		// 이벤트 처리 ...
		webHookService.callEvent();
		return "이벤트 발생!";
	}
}