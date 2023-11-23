package com.wanted.restaurant.boundedContext.evalutation.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.wanted.restaurant.base.rsData.RsData;
import com.wanted.restaurant.boundedContext.evalutation.dto.EvaluateRequestDto;
import com.wanted.restaurant.boundedContext.evalutation.service.EvaluationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@Tag(name = "EvaluationController", description = "식당 평가 컨트롤러")
public class EvaluationController {
	private final EvaluationService evaluationService;

	//평가 생성
	@PostMapping("/evaluate")
	@Operation(summary = "식당 평가 생성", security = @SecurityRequirement(name = "bearerAuth"))
	public ResponseEntity<RsData> evaluateRestaurant(@RequestBody EvaluateRequestDto requestDto) {
		return ResponseEntity.ok().body(evaluationService.evaluate(requestDto));

	}
}
