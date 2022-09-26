package com.payhere.accountbook.domain.accountbook.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.payhere.accountbook.domain.accountbook.model.Book;

public interface BookRepository extends JpaRepository<Book, Long> {

}
