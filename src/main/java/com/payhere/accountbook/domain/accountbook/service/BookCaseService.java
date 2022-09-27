package com.payhere.accountbook.domain.accountbook.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.payhere.accountbook.domain.accountbook.controller.dto.BookCaseDeleteRequest;
import com.payhere.accountbook.domain.accountbook.controller.dto.BookCaseRegisterRequest;
import com.payhere.accountbook.domain.accountbook.controller.dto.BookCaseUpdateRequest;
import com.payhere.accountbook.domain.accountbook.model.Book;
import com.payhere.accountbook.domain.accountbook.model.BookCase;
import com.payhere.accountbook.domain.accountbook.repository.BookCaseRepository;
import com.payhere.accountbook.domain.accountbook.repository.BookRepository;
import com.payhere.accountbook.domain.accountbook.service.dto.BookCaseResponse;
import com.payhere.accountbook.domain.accountbook.service.dto.BookCaseResponses;
import com.payhere.accountbook.global.error.exception.BookException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookCaseService {
	private final BookRepository bookRepository;
	private final BookCaseRepository bookCaseRepository;

	@Transactional(readOnly = true)
	public BookCaseResponse find(Long bookCaseId) {
		BookCase bookCase = bookCaseRepository.findById(bookCaseId).orElseThrow(() -> {
			throw BookException.notFoundBookCaseById(bookCaseId);
		});

		return BookConverter.toBookCaseResponse(bookCase);
	}

	@Transactional(readOnly = true)
	public BookCaseResponses findBookCases(Long bookId) {
		Book book = bookRepository.findById(bookId).orElseThrow(() -> {
			throw BookException.notFoundBookById(bookId);
		});
		List<BookCase> bookCases = bookCaseRepository.findByBook(book);
		List<BookCaseResponse> bookCaseResponses = new ArrayList<>();
		for(BookCase bookCase : bookCases){
			bookCaseResponses.add(BookConverter.toBookCaseResponse(bookCase));
		}

		return new BookCaseResponses(bookCaseResponses);
	}

	@Transactional
	public BookCaseResponse register(Long bookId, BookCaseRegisterRequest bookCaseRegisterRequest) {
		Book book = bookRepository.findById(bookId).orElseThrow(() -> {
			throw BookException.notFoundBookById(bookId);
		});
		BookCase bookCase = BookConverter.toBookCase(book, bookCaseRegisterRequest);
		BookCase savedBook = bookCaseRepository.save(bookCase);
		book.changeIncomeAndOutcome(savedBook.getIncome(), savedBook.getOutcome());

		return BookConverter.toBookCaseResponse(savedBook);
	}

	@Transactional
	public BookCaseResponse update(BookCaseUpdateRequest bookCaseUpdateRequest) {
		Long bookCaseId = bookCaseUpdateRequest.id();
		BookCase bookCase = bookCaseRepository.findById(bookCaseId).orElseThrow(() -> {
			throw BookException.notFoundBookCaseById(bookCaseId);
		});

		bookCase.change(bookCaseUpdateRequest.income(), bookCaseUpdateRequest.outcome(), bookCaseUpdateRequest.title(),
			bookCaseUpdateRequest.place(), bookCaseUpdateRequest.memo());

		return BookConverter.toBookCaseResponse(bookCase);
	}

	@Transactional
	public BookCaseResponse delete(BookCaseDeleteRequest bookCaseDeleteRequest) {
		Long bookCaseId = bookCaseDeleteRequest.id();
		BookCase bookCase = bookCaseRepository.findBookCaseAndBookById(bookCaseId).orElseThrow(() -> {
			throw BookException.notFoundBookCaseById(bookCaseId);
		});
		Book book = bookCase.getBook();
		book.changeIncomeAndOutcome((-1) * bookCase.getIncome(), (-1) * bookCase.getOutcome());
		bookCaseRepository.delete(bookCase);

		return BookConverter.toBookCaseResponse(bookCase);
	}
}
