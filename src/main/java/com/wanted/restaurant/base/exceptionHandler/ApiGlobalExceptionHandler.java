package com.wanted.restaurant.base.exceptionHandler;

import static org.springframework.http.HttpStatus.*;

import java.util.stream.Collectors;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.wanted.restaurant.base.rsData.RsData;

@RestControllerAdvice(annotations = {RestController.class}) // 모든 @RestController 에서 발생한 예외에 대한 제어권을 가로챈다.
public class ApiGlobalExceptionHandler {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(NOT_FOUND)
	public RsData<String> errorHandler(MethodArgumentNotValidException exception) {
		String msg = exception
			.getBindingResult()
			.getAllErrors()
			.stream()
			.map(DefaultMessageSourceResolvable::getDefaultMessage)
			.collect(Collectors.joining("/"));

		String data = exception
			.getBindingResult()
			.getAllErrors()
			.stream()
			.map(DefaultMessageSourceResolvable::getCode)
			.collect(Collectors.joining("/"));

		return RsData.of("F-MethodArgumentNotValidException", msg, data);
	}

	@ExceptionHandler(RuntimeException.class)
	@ResponseStatus(INTERNAL_SERVER_ERROR)
	public RsData<String> errorHandler(RuntimeException exception) {
		String msg = exception.getClass().toString();

		String data = exception.getMessage();

		return RsData.of("F-RuntimeException", msg, data);
	}

	@ExceptionHandler(AuthenticationException.class)
	@ResponseStatus(UNAUTHORIZED) // 예외에 따른 HTTP 상태 코드 설정
	public RsData<String> handleAuthenticationException(AuthenticationException exception) {
		String msg = exception.getMessage(); // 예외 메시지 추출

		// RsData.of 메서드 호출
		return RsData.of("F-AuthenticationException", msg);
	}
}