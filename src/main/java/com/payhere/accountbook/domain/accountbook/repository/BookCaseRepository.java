package com.payhere.accountbook.domain.accountbook.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.payhere.accountbook.domain.accountbook.model.BookCase;

public interface BookCaseRepository extends JpaRepository<BookCase, Long> {

	@Query("select bc from BookCase bc join fetch bc.book b")
	Optional<BookCase> findBookCaseAndBookById(Long bookCaseId);

}
