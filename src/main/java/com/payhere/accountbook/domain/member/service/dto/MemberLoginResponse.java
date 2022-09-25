package com.payhere.accountbook.domain.member.service.dto;

public record MemberLoginResponse(
	MemberResponse memberResponse,
	String accessToken
) {
}
