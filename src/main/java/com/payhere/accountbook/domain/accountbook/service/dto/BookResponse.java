package com.payhere.accountbook.domain.accountbook.service.dto;

import lombok.Builder;

public record BookResponse(
	Long id,
	Long year,
	Long month,
	Long day,
	Long income,
	Long outcome,
	String title
){
	@Builder
	public BookResponse {
	}
}
