package com.payhere.accountbook.domain.member.controller.dto;

import javax.validation.constraints.NotNull;

public record MemberIdRequest (
	@NotNull
	Long id
){
}
