package com.payhere.accountbook.domain.accountbook.controller.dto;

import javax.validation.constraints.NotNull;

public record BookCaseReviveRequest(
	@NotNull
	Long id
) {
}
