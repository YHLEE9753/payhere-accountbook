package com.payhere.accountbook.domain.accountbook.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.payhere.accountbook.domain.accountbook.controller.dto.BookCaseDeleteRequest;
import com.payhere.accountbook.domain.accountbook.controller.dto.BookCaseRegisterRequest;
import com.payhere.accountbook.domain.accountbook.controller.dto.BookCaseUpdateRequest;
import com.payhere.accountbook.domain.accountbook.model.Book;
import com.payhere.accountbook.domain.accountbook.model.BookCase;
import com.payhere.accountbook.domain.accountbook.repository.BookCaseRepository;
import com.payhere.accountbook.domain.accountbook.repository.BookRepository;
import com.payhere.accountbook.domain.accountbook.service.dto.BookCaseResponse;
import com.payhere.accountbook.domain.member.model.Member;
import com.payhere.accountbook.domain.member.repository.MemberRepository;

@SpringBootTest
class BookCaseServiceTest {
	@Autowired
	BookCaseService bookCaseService;
	@Autowired
	BookCaseRepository bookCaseRepository;
	@Autowired
	BookRepository bookRepository;
	@Autowired
	MemberRepository memberRepository;

	Member member;
	Book book;
	BookCase bookCase;

	@BeforeEach
	void beforeEach() {
		member = memberRepository.save(new Member("test999@google.com", "qwer1234", "bookCaseTest"));
		book = bookRepository.save(Book.builder()
			.member(member)
			.year(2022L)
			.month(9L)
			.day(27L)
			.title("happy day")
			.build());
		bookCase = bookCaseRepository.save(BookCase.builder()
			.book(book)
			.income(3000L)
			.outcome(1500L)
			.title("e-mart shopping")
			.place("e-mart")
			.build()
		);
	}

	@AfterEach
	void afterEach() {
		bookCaseRepository.deleteAllInBatch();
		bookRepository.deleteAllInBatch();
		memberRepository.deleteAllInBatch();
	}

	@Test
	@DisplayName("가계부(단건)을 id 로 찾는다.")
	void findBookCaseTest () {
	    // given
		Long bookCaseId = bookCase.getId();

		// when
		BookCaseResponse bookCaseResponse = bookCaseService.find(bookCaseId);

		// then
		assertAll(
			() -> assertThat(bookCaseResponse.id()).isEqualTo(bookCaseId),
			() -> assertThat(bookCaseResponse.income()).isEqualTo(3000),
			() -> assertThat(bookCaseResponse.outcome()).isEqualTo(1500),
			() -> assertThat(bookCaseResponse.title()).isEqualTo("e-mart shopping"),
			() -> assertThat(bookCaseResponse.place()).isEqualTo("e-mart")
		);
	}

	@Test
	@DisplayName("가계부(단건)을 생성한다")
	void registerBookCaseTest () {
	    // given
		BookCaseRegisterRequest bookCaseRegisterRequest = BookCaseRegisterRequest.builder()
			.bookId(book.getId())
			.income(4500L)
			.outcome(1500L)
			.title("ice coffee")
			.place("mega coffee")
			.build();

	    // when
		BookCaseResponse bookCaseResponse = bookCaseService.register(bookCaseRegisterRequest);

		// then
		assertAll(
			() -> assertThat(bookCaseResponse.id()).isNotNull(),
			() -> assertThat(bookCaseResponse.income()).isEqualTo(4500),
			() -> assertThat(bookCaseResponse.outcome()).isEqualTo(1500),
			() -> assertThat(bookCaseResponse.title()).isEqualTo("ice coffee"),
			() -> assertThat(bookCaseResponse.place()).isEqualTo("mega coffee")
		);
	}

	@Test
	@DisplayName("가계부(단건)을 수정한다")
	void updateBookCaseTest () {
	    // given
		BookCaseUpdateRequest bookCaseUpdateRequest = BookCaseUpdateRequest.builder()
			.bookId(book.getId())
			.id(bookCase.getId())
			.income(7000L)
			.outcome(4000L)
			.title("change title")
			.place("change place")
			.build();

	    // when
		BookCaseResponse bookCaseResponse = bookCaseService.update(bookCaseUpdateRequest);

		// then
		assertAll(
			() -> assertThat(bookCaseResponse.id()).isEqualTo(bookCase.getId()),
			() -> assertThat(bookCaseResponse.income()).isEqualTo(7000),
			() -> assertThat(bookCaseResponse.outcome()).isEqualTo(4000),
			() -> assertThat(bookCaseResponse.title()).isEqualTo("change title"),
			() -> assertThat(bookCaseResponse.place()).isEqualTo("change place")
		);
	}

	@Test
	@DisplayName("가계부(단건)을 삭제한다")
	void deleteBookCaseTest () {
	    // given
		BookCaseDeleteRequest bookCaseDeleteRequest = new BookCaseDeleteRequest(book.getId(), bookCase.getId());

	    // when
		BookCaseResponse bookCaseResponse = bookCaseService.delete(bookCaseDeleteRequest);

		// then
		Long id = bookCaseResponse.id();
		BookCase softDeletedBookCase = bookCaseRepository.findBookCaseByIdIgnoreWhere(id).get();

		assertAll(
			() -> assertThat(softDeletedBookCase.getId()).isEqualTo(bookCase.getId()),
			() -> assertThat(softDeletedBookCase.getIncome()).isEqualTo(3000),
			() -> assertThat(softDeletedBookCase.getOutcome()).isEqualTo(1500),
			() -> assertThat(softDeletedBookCase.getTitle()).isEqualTo("e-mart shopping"),
			() -> assertThat(softDeletedBookCase.getPlace()).isEqualTo("e-mart"),
			() -> assertThat(softDeletedBookCase.isDelete()).isTrue()
		);
	}
}