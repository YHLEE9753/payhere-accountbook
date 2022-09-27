package com.payhere.accountbook.domain.accountbook.service;

import com.payhere.accountbook.domain.accountbook.controller.dto.BookCaseRegisterRequest;
import com.payhere.accountbook.domain.accountbook.controller.dto.BookRegisterRequest;
import com.payhere.accountbook.domain.accountbook.model.Book;
import com.payhere.accountbook.domain.accountbook.model.BookCase;
import com.payhere.accountbook.domain.accountbook.service.dto.BookCaseResponse;
import com.payhere.accountbook.domain.accountbook.service.dto.BookResponse;
import com.payhere.accountbook.domain.member.model.Member;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BookConverter {
	public static Book toBook(Member member, BookRegisterRequest bookRegisterRequest) {
		return Book.builder()
			.member(member)
			.year(bookRegisterRequest.year())
			.month(bookRegisterRequest.month())
			.day(bookRegisterRequest.day())
			.title(bookRegisterRequest.title())
			.build();
	}

	public static BookResponse toBookResponse(Book savedBook) {
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

	public static BookCaseResponse toBookCaseResponse(BookCase bookCase) {
		return BookCaseResponse.builder()
			.id(bookCase.getId())
			.income(bookCase.getIncome())
			.outcome(bookCase.getOutcome())
			.title(bookCase.getTitle())
			.place(bookCase.getPlace())
			.build();
	}

	public static BookCase toBookCase(Book book, BookCaseRegisterRequest bookCaseRegisterRequest) {
		return BookCase.builder()
			.book(book)
			.income(bookCaseRegisterRequest.income())
			.outcome(bookCaseRegisterRequest.outcome())
			.title(bookCaseRegisterRequest.title())
			.place(bookCaseRegisterRequest.place())
			.build();
	}
}
