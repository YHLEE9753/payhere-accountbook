package com.payhere.accountbook.domain.accountbook.controller.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Builder;

public record BookCaseUpdateRequest (
	@NotNull
	Long bookId,
	@NotNull
	Long id,
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
	public BookCaseUpdateRequest {
	}
}