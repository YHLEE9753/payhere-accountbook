package com.payhere.accountbook.domain.accountbook.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.payhere.accountbook.domain.accountbook.model.Memo;

public interface MemoRepository extends JpaRepository<Memo, Long> {

}
