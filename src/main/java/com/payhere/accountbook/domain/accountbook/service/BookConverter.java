package com.payhere.accountbook.domain.accountbook.service;

import com.payhere.accountbook.domain.accountbook.controller.dto.BookRegisterRequest;
import com.payhere.accountbook.domain.accountbook.model.Book;
import com.payhere.accountbook.domain.accountbook.service.dto.BookResponse;
import com.payhere.accountbook.domain.member.model.Member;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class BookConverter {
	public static Book toAccountBook(Member member, BookRegisterRequest bookRegisterRequest) {
		return Book.builder()
			.member(member)
			.year(bookRegisterRequest.year())
			.month(bookRegisterRequest.month())
			.day(bookRegisterRequest.day())
			.title(bookRegisterRequest.title())
			.build();
	}

	public static BookResponse toAccountBookResponse(Book savedBook) {
		return BookResponse.builder()
			.id(savedBook.getId())
			.year(savedBook.getYear())
			.month(savedBook.getMonth())
			.day(savedBook.getDay())
			.income(savedBook.getIncome())
			.outcome(savedBook.getOutcome())
			.title(savedBook.getTitle())
			.build();
	}
}
