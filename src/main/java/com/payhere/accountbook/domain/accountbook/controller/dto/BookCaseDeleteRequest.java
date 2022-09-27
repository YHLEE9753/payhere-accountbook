package com.payhere.accountbook.domain.accountbook.controller.dto;

import javax.validation.constraints.NotNull;

public record BookCaseDeleteRequest(
	@NotNull
	Long bookId,
	@NotNull
	Long id
) {
}
