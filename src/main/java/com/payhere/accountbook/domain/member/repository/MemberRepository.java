package com.payhere.accountbook.domain.member.repository;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.payhere.accountbook.domain.member.model.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {
	Optional<Member> findByAccessTokenValue(String accessTokenValue);
}