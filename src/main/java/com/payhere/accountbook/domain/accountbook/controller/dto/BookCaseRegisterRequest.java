package com.payhere.accountbook.domain.accountbook.controller.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Builder;

public record BookCaseRegisterRequest(
	@NotNull
	Long income,
	@NotNull
	Long outcome,
	@NotBlank
	String title,
	@NotBlank
	String place,
	String memo
) {
	@Builder
	public BookCaseRegisterRequest {
	}
}
