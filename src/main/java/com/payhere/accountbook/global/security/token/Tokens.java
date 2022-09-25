package com.payhere.accountbook.global.security.token;

public record Tokens(
	String accessToken,
	String refreshToken
) {
}
