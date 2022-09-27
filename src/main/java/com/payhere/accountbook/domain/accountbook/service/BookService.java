package com.payhere.accountbook.domain.accountbook.service;


import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.payhere.accountbook.domain.accountbook.controller.dto.BookRegisterRequest;
import com.payhere.accountbook.domain.accountbook.controller.dto.BookUpdateRequest;
import com.payhere.accountbook.domain.accountbook.model.Book;
import com.payhere.accountbook.domain.accountbook.repository.BookRepository;
import com.payhere.accountbook.domain.accountbook.service.dto.BookResponse;
import com.payhere.accountbook.domain.accountbook.service.dto.BookResponses;
import com.payhere.accountbook.domain.member.model.Member;
import com.payhere.accountbook.domain.member.repository.MemberRepository;
import com.payhere.accountbook.global.error.exception.BookException;
import com.payhere.accountbook.global.error.exception.MemberException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookService {
	private final BookRepository bookRepository;
	private final MemberRepository memberRepository;

	@Transactional(readOnly = true)
	public BookResponse find(Long bookId) {
		Book book = bookRepository.findById(bookId).orElseThrow(() -> {
			throw BookException.notFoundBookById(bookId);
		});

		return BookConverter.toBookResponse(book);
	}

	@Transactional(readOnly = true)
	public BookResponses findBooks(Long memberId) {
		Member member = memberRepository.findById(memberId).orElseThrow(() -> {
			throw MemberException.notFoundMemberById(memberId);
		});
		List<Book> books = bookRepository.findByMember(member);
		List<BookResponse> bookResponses = new ArrayList<>();
		for(Book book : books){
			bookResponses.add(BookConverter.toBookResponse(book));
		}

		return new BookResponses(bookResponses);
	}

	@Transactional
	public BookResponse register(Long memberId, BookRegisterRequest bookRegisterRequest) {
		Member member = memberRepository.findById(memberId).orElseThrow(() -> {
			throw MemberException.notFoundMemberById(memberId);
		});
		Book book = BookConverter.toBook(member, bookRegisterRequest);
		Book savedBook = bookRepository.save(book);

		return BookConverter.toBookResponse(savedBook);
	}

	@Transactional
	public BookResponse update(BookUpdateRequest bookUpdateRequest) {
		Long bookId = bookUpdateRequest.id();
		Book book = bookRepository.findById(bookId).orElseThrow(() -> {
			throw BookException.notFoundBookById(bookId);
		});

		book.changeTitleAndMemo(bookUpdateRequest.title(), bookUpdateRequest.memo());

		return BookConverter.toBookResponse(book);


	}
}
