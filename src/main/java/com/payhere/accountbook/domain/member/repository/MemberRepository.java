package com.payhere.accountbook.domain.member.repository;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.payhere.accountbook.domain.member.model.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {

	@Query("select m from Member m where m.accessToken = :accessToken")
	Optional<Member> findByAccessTokenValue(String accessToken);

	@Query("select m from Member m where m.email = :email")
	Optional<Member> findByEmail(String email);

	@Query("select m from Member m where m.nickname = :nickname")
	Optional<Member> findByNickname(String nickname);
}