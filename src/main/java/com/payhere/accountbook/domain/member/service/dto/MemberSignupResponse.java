package com.payhere.accountbook.domain.member.service.dto;

public record MemberSignupResponse (
	MemberResponse memberResponse,
	String accessToken
){
}
