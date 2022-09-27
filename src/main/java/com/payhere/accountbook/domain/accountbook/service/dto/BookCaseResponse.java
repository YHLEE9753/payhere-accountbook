package com.payhere.accountbook.domain.accountbook.service.dto;

import lombok.Builder;

public record BookCaseResponse(
	Long id,
	Long income,
	Long outcome,
	String title,
	String place,
	String memo
) {
	@Builder
	public BookCaseResponse {
	}
}
