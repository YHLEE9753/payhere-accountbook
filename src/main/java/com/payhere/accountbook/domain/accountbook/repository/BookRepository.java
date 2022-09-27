package com.payhere.accountbook.domain.accountbook.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.payhere.accountbook.domain.accountbook.model.Book;
import com.payhere.accountbook.domain.member.model.Member;

public interface BookRepository extends JpaRepository<Book, Long> {
	List<Book> findByMember(Member member);
}
