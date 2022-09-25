package com.payhere.accountbook.domain.member.controller.dto;

import javax.validation.constraints.NotBlank;

public record MemberLoginRequest(
	@NotBlank
	String email,
	@NotBlank
	String password
) {
}
