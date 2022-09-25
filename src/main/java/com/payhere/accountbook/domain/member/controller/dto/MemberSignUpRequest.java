package com.payhere.accountbook.domain.member.controller.dto;

import javax.validation.constraints.NotBlank;

public record MemberSignUpRequest(
	@NotBlank
	String email,
	@NotBlank
	String nickname,
	@NotBlank
	String password
){
}
