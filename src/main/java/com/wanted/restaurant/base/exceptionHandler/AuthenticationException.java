package com.wanted.restaurant.base.exceptionHandler;

public class AuthenticationException extends RuntimeException {
	public AuthenticationException(String message) {
		super(message);
	}
}