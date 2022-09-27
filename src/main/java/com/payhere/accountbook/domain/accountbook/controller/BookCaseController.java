package com.payhere.accountbook.domain.accountbook.controller;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.payhere.accountbook.domain.accountbook.controller.dto.BookCaseDeleteRequest;
import com.payhere.accountbook.domain.accountbook.controller.dto.BookCaseRegisterRequest;
import com.payhere.accountbook.domain.accountbook.controller.dto.BookCaseUpdateRequest;
import com.payhere.accountbook.domain.accountbook.service.BookCaseService;
import com.payhere.accountbook.domain.accountbook.service.dto.BookCaseResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/book")
@RequiredArgsConstructor
public class BookCaseController {
	private final BookCaseService bookCaseService;

	@GetMapping("/{bookId}/bookcase/{bookCaseId}")
	public ResponseEntity<BookCaseResponse> bookCase(
		@PathVariable Long bookCaseId
	) {
		BookCaseResponse bookCaseResponse = bookCaseService.find(bookCaseId);

		return ResponseEntity
			.ok()
			.body(bookCaseResponse);
	}

	@PostMapping("/{bookId}/bookcase")
	public ResponseEntity<BookCaseResponse> bookCase(
		@PathVariable Long bookId,
		@Valid @RequestBody BookCaseRegisterRequest bookCaseRegisterRequest
	) {
		BookCaseResponse bookCaseResponse = bookCaseService.register(bookId,bookCaseRegisterRequest);

		return ResponseEntity
			.ok()
			.body(bookCaseResponse);
	}

	@PatchMapping("/{bookId}/bookcase")
	public ResponseEntity<BookCaseResponse> bookCase(
		@Valid @RequestBody BookCaseUpdateRequest bookCaseUpdateRequest
	) {
		BookCaseResponse bookCaseResponse = bookCaseService.update(bookCaseUpdateRequest);

		return ResponseEntity
			.ok()
			.body(bookCaseResponse);
	}

	@DeleteMapping("/{bookId}/bookcase")
	public ResponseEntity<BookCaseResponse> book(
		@Valid @RequestBody BookCaseDeleteRequest bookCaseDeleteRequest
	) {
		BookCaseResponse bookCaseResponse = bookCaseService.delete(bookCaseDeleteRequest);

		return ResponseEntity
			.ok()
			.body(bookCaseResponse);
	}
}
