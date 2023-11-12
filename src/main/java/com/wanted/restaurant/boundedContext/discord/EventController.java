package com.wanted.restaurant.boundedContext.discord;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/event")
public class EventController {

	private final WebHookService webHookService;

	@GetMapping("")
	public String postEvent() {
		// 이벤트 처리 ...
		webHookService.callEvent();
		return "이벤트 발생!";
	}
}