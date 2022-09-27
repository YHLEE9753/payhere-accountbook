package com.payhere.accountbook.domain.accountbook.controller.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public record BookUpdateRequest(
	@NotNull
	Long id,
	@NotBlank
	String title,
	String memo
) {
}
