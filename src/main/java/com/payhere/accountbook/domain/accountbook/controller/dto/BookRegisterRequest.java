package com.payhere.accountbook.domain.accountbook.controller.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Builder;

public record BookRegisterRequest(
	@NotNull
	Long year,
	@NotNull
	Long month,
	@NotNull
	Long day,
	@NotBlank
	String title,
	String memo
) {
	@Builder
	public BookRegisterRequest {
	}
}
