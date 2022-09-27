package com.payhere.accountbook.domain.accountbook.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.payhere.accountbook.domain.accountbook.model.Book;
import com.payhere.accountbook.domain.accountbook.model.BookCase;

public interface BookCaseRepository extends JpaRepository<BookCase, Long> {

	@Query("select bc from BookCase bc join fetch bc.book where bc.id = :bookCaseId")
	Optional<BookCase> findBookCaseAndBookById(@Param("bookCaseId") Long bookCaseId);

	@Query(value = "select * from book_case where book_case_id = :bookCaseId", nativeQuery = true)
	Optional<BookCase> findBookCaseByIdIgnoreWhere(@Param("bookCaseId") Long bookCaseId);

	List<BookCase> findByBook(Book book);
}
