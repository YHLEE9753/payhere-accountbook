package com.payhere.accountbook.domain.accountbook.controller.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Builder;

public record BookCaseRegisterRequest(
	@NotNull
	Long bookId,
	@NotNull
	Long income,
	@NotNull
	Long outcome,
	@NotBlank
	String title,
	@NotBlank
	String place
) {
	@Builder
	public BookCaseRegisterRequest {
	}
}
