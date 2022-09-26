package com.payhere.accountbook.global.error.dto;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public enum ErrorCode {

	// Common
	INVALID_METHOD_ARGUMENT("C001", "Invalid method argument", HttpStatus.BAD_REQUEST),
	UNKNOWN_SERVER_ERROR("C002", "Unknown server error", HttpStatus.INTERNAL_SERVER_ERROR),
	METHOD_NOT_ALLOWED("C003", "Http method not allowed", HttpStatus.METHOD_NOT_ALLOWED),

	// Member
	NOT_FOUND_MEMBER_BY_ID("M001", "Not found member by id", HttpStatus.BAD_REQUEST),
	EMAIL_DUPLICATED_MEMBER("M002", "Already existed email member", HttpStatus.BAD_REQUEST),
	NICKNAME_DUPLICATED_MEMBER("M003", "Already existed nickname member", HttpStatus.BAD_REQUEST),
	NOT_FOUND_MEMBER_BY_EMAIL("M004", "Not found member by email", HttpStatus.BAD_REQUEST),
	INVALID_PASSWORD("M005", "Password is not same", HttpStatus.BAD_REQUEST),
	BlACK_TYPE_TOKEN_DETECTION("M006", "Black type token is detected", HttpStatus.BAD_REQUEST),
	NOT_FOUND_MEMBER_BY_ACCESS_TOKEN("M007", "Not found member by access token", HttpStatus.BAD_REQUEST),
	INVALID_EMAIL("M008", "Email is invalid", HttpStatus.BAD_REQUEST),

	// Book
	NOT_FOUND_BOOK_BY_ID("B001", "Not found book by id", HttpStatus.BAD_REQUEST),

	// Token Expiration
	ACCESS_TOKEN_EXPIRATION("T001", "Access token is expired", HttpStatus.BAD_REQUEST),
	REFRESH_TOKEN_EXPIRATION("T002", "Refresh token is expired", HttpStatus.BAD_REQUEST);

	private final String code;
	private final String message;
	private final HttpStatus httpStatus;

	ErrorCode(String code, String message, HttpStatus httpStatus) {
		this.code = code;
		this.message = message;
		this.httpStatus = httpStatus;
	}
}
