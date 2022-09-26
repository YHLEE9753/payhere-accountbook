package com.payhere.accountbook.domain.accountbook.controller;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.payhere.accountbook.domain.accountbook.controller.dto.BookRegisterRequest;
import com.payhere.accountbook.domain.accountbook.controller.dto.BookUpdateRequest;
import com.payhere.accountbook.domain.accountbook.service.BookService;
import com.payhere.accountbook.domain.accountbook.service.dto.BookResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/book")
@RequiredArgsConstructor
public class BookController {
	private final BookService bookService;

	@GetMapping("/{bookId}")
	public ResponseEntity<BookResponse> book(
		@PathVariable Long bookId
	) {
		BookResponse bookResponse = bookService.find(bookId);

		return ResponseEntity
			.ok()
			.body(bookResponse);
	}

	@PostMapping
	public ResponseEntity<BookResponse> book(
		@AuthenticationPrincipal Long memberId,
		@Valid @RequestBody BookRegisterRequest bookRegisterRequest
	) {
		BookResponse bookResponse = bookService.register(memberId, bookRegisterRequest);

		return ResponseEntity
			.ok()
			.body(bookResponse);
	}

	@PatchMapping
	public ResponseEntity<BookResponse> book(
		@Valid @RequestBody BookUpdateRequest bookUpdateRequest
	){
		BookResponse bookResponse = bookService.update(bookUpdateRequest);

		return ResponseEntity
			.ok()
			.body(bookResponse);
	}

}
