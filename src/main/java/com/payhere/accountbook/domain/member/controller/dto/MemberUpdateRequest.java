package com.payhere.accountbook.domain.member.controller.dto;

import javax.validation.constraints.NotBlank;

public record MemberUpdateRequest(
	@NotBlank
	String nickname,
	@NotBlank
	String password
) {
}
