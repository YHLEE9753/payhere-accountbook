package com.payhere.accountbook.domain.member.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.payhere.accountbook.domain.member.model.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {

	@Query("select m from Member m where m.accessToken = :accessToken")
	Optional<Member> findByAccessTokenValue(@Param("accessToken") String accessToken);

	@Query("select m from Member m where m.email = :email")
	Optional<Member> findByEmail(@Param("email") String email);

	@Query("select m from Member m where m.nickname = :nickname")
	Optional<Member> findByNickname(@Param("nickname") String nickname);
}