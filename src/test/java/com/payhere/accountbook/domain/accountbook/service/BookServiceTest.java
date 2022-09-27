package com.payhere.accountbook.domain.accountbook.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.payhere.accountbook.domain.accountbook.controller.dto.BookRegisterRequest;
import com.payhere.accountbook.domain.accountbook.controller.dto.BookUpdateRequest;
import com.payhere.accountbook.domain.accountbook.model.Book;
import com.payhere.accountbook.domain.accountbook.repository.BookRepository;
import com.payhere.accountbook.domain.accountbook.service.dto.BookResponse;
import com.payhere.accountbook.domain.member.model.Member;
import com.payhere.accountbook.domain.member.repository.MemberRepository;

@SpringBootTest
class BookServiceTest {
	@Autowired
	BookService bookService;
	@Autowired
	BookRepository bookRepository;
	@Autowired
	MemberRepository memberRepository;
	Book book;
	Member member;

	@BeforeEach
	void beforeEach() {
		member = memberRepository.save(new Member("test123@google.com", "qwer1234", "whale123"));
		book = bookRepository.save(Book.builder()
			.member(member)
			.year(2022L)
			.month(9L)
			.day(27L)
			.title("happy day")
			.build());
	}

	@AfterEach
	void afterEach() {
		bookRepository.deleteAllInBatch();
		memberRepository.deleteAllInBatch();
	}

	@Test
	@DisplayName("가계부(일)을 id 로 찾는다")
	void findBookTest() {
		// given
		Long bookId = book.getId();

		// when
		BookResponse bookResponse = bookService.find(bookId);

		// then
		assertAll(
			() -> assertThat(bookResponse.id()).isEqualTo(bookId),
			() -> assertThat(bookResponse.year()).isEqualTo(2022),
			() -> assertThat(bookResponse.month()).isEqualTo(9),
			() -> assertThat(bookResponse.day()).isEqualTo(27),
			() -> assertThat(bookResponse.income()).isEqualTo(0),
			() -> assertThat(bookResponse.outcome()).isEqualTo(0),
			() -> assertThat(bookResponse.title()).isEqualTo("happy day")
		);
	}

	@Test
	@DisplayName("가계부(일)를 생성한다")
	void registerBookTest() {
		// given
		BookRegisterRequest bookRegisterRequest = BookRegisterRequest.builder()
			.year(2022L)
			.month(8L)
			.day(22L)
			.title("hello August")
			.build();

		// when
		BookResponse bookResponse = bookService.register(member.getId(), bookRegisterRequest);

		// then
		assertAll(
			() -> assertThat(bookResponse.id()).isNotNull(),
			() -> assertThat(bookResponse.year()).isEqualTo(2022),
			() -> assertThat(bookResponse.month()).isEqualTo(8),
			() -> assertThat(bookResponse.day()).isEqualTo(22),
			() -> assertThat(bookResponse.income()).isEqualTo(0),
			() -> assertThat(bookResponse.outcome()).isEqualTo(0),
			() -> assertThat(bookResponse.title()).isEqualTo("hello August")
		);
	}

	@Test
	@DisplayName("가계부(일)를 수정한다")
	void updateBookTest() {
		// given
		BookUpdateRequest bookUpdateRequest = new BookUpdateRequest(book.getId(), "changed title");

		// when
		BookResponse bookResponse = bookService.update(bookUpdateRequest);

		// then
		assertAll(
			() -> assertThat(bookResponse.id()).isNotNull(),
			() -> assertThat(bookResponse.year()).isEqualTo(2022),
			() -> assertThat(bookResponse.month()).isEqualTo(9),
			() -> assertThat(bookResponse.day()).isEqualTo(27),
			() -> assertThat(bookResponse.income()).isEqualTo(0),
			() -> assertThat(bookResponse.outcome()).isEqualTo(0),
			() -> assertThat(bookResponse.title()).isEqualTo("changed title")
		);
	}
}