package com.payhere.accountbook.global.error.exception;

import java.text.MessageFormat;

import com.payhere.accountbook.global.error.dto.ErrorCode;

public class BookException extends BusinessException {

	protected BookException(ErrorCode errorCode, String message) {
		super(errorCode, message);
	}

	protected BookException(ErrorCode errorCode, String message, Throwable cause) {
		super(errorCode, message, cause);
	}

	public static BookException notFoundBookById(Long bookId) {
		return new BookException(ErrorCode.NOT_FOUND_BOOK_BY_ID,
			MessageFormat.format("가계부(일)을 id로 찾을 수 없습니다. (id: {0})", bookId));
	}

	public static BookException notFoundBookCaseById(Long bookCaseId) {
		return new BookException(ErrorCode.NOT_FOUND_BOOK_CASE_BY_ID,
			MessageFormat.format("가계부 단건 케이스를 id로 찾을 수 없습니다. (id: {0})", bookCaseId));
	}
}