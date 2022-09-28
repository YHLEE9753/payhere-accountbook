package com.payhere.accountbook.domain.member.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;


import javax.transaction.Transactional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.payhere.accountbook.domain.member.controller.dto.MemberLoginRequest;
import com.payhere.accountbook.domain.member.controller.dto.MemberSignUpRequest;
import com.payhere.accountbook.domain.member.model.Member;
import com.payhere.accountbook.domain.member.repository.MemberRepository;
import com.payhere.accountbook.domain.member.service.dto.MemberResponse;
import com.payhere.accountbook.global.error.exception.MemberException;
import com.payhere.accountbook.global.security.token.Tokens;

@SpringBootTest
@Transactional
class AuthenticationServiceTest {
	@Autowired
	MemberRepository memberRepository;
	@Autowired
	AuthenticationService authenticationService;
	@Autowired
	PasswordEncoder passwordEncoder;

	Member member1;

	@BeforeEach
	void beforeEach() {
		member1 = new Member("test123@google.com", passwordEncoder.encode("qwer1234"), "whale123");
		Member member2 = new Member("test456@google.com", passwordEncoder.encode("qwer1234"), "dolphin123");
		memberRepository.save(member1);
		memberRepository.save(member2);
	}

	@AfterEach
	void afterEach() {
		memberRepository.deleteAllInBatch();
	}

	@Test
	@DisplayName("회원가입에 성공한다")
	void signUpMemberTest() {
		// given
		MemberSignUpRequest memberSignUpRequest = new MemberSignUpRequest("test789@google.com", "lion123",
			"qwer1234");

		// when
		MemberResponse memberResponse = authenticationService.signupMember(memberSignUpRequest);

		// then
		assertAll(
			() -> assertThat(memberResponse.id()).isNotNull(),
			() -> assertThat(memberResponse.email()).isEqualTo("test789@google.com"),
			() -> assertThat(memberResponse.nickName()).isEqualTo("lion123")
		);
	}

	@Test
	@DisplayName("중복된 이메일로는 회원가입이 불가능하다")
	void duplicatedEmailTest() {
		// given
		MemberSignUpRequest memberSignUpRequest = new MemberSignUpRequest("test123@google.com", "lion123",
			"qwer1234");

		// when // then
		assertThatThrownBy(() -> authenticationService.signupMember(memberSignUpRequest))
			.isInstanceOf(MemberException.class)
			.hasMessageContaining("이미 등록된 회원 입니다. (email: test123@google.com)");
	}

	@Test
	@DisplayName("중복된 닉네임으로는 회원가입이 불가능하다")
	void duplicatedNicknameTest() {
		// given
		MemberSignUpRequest memberSignUpRequest = new MemberSignUpRequest("test789@google.com", "whale123",
			"qwer1234");

		// when // then
		assertThatThrownBy(() -> authenticationService.signupMember(memberSignUpRequest))
			.isInstanceOf(MemberException.class)
			.hasMessageContaining("이미 등록된 회원 입니다. (nickname: whale123)");
	}

	@Test
	@DisplayName("로그인을 성공한다")
	void loginTest() {
		// given
		MemberLoginRequest memberLoginRequest = new MemberLoginRequest("test123@google.com", "qwer1234");

		// when
		MemberResponse memberResponse = authenticationService.login(memberLoginRequest);

		// then
		assertAll(
			() -> assertThat(memberResponse.id()).isNotNull(),
			() -> assertThat(memberResponse.email()).isEqualTo("test123@google.com"),
			() -> assertThat(memberResponse.nickName()).isEqualTo("whale123")
		);
	}

	@Test
	@DisplayName("잘못된 비밀번호로 로그인에 실패한다.")
	void invalidPasswordTest() {
		// given
		MemberLoginRequest memberLoginRequest = new MemberLoginRequest("test123@google.com", "invalid1");

		// when // then
		assertThatThrownBy(() -> authenticationService.login(memberLoginRequest))
			.isInstanceOf(MemberException.class)
			.hasMessageContaining("비밀번호가 유효하지 않습니다.");
	}

	@Test
	@DisplayName("로그아웃을 진행한다")
	void logoutTest() {
		// given
		Long memberId = member1.getId();
		String accessTokenWithBlackListType = "BL blackToken";

		// when
		authenticationService.logout(memberId, accessTokenWithBlackListType);
		Member updatedMember = memberRepository.findById(memberId).get();

		// then
		assertAll(
			() -> assertThat(updatedMember.getAccessToken()).isEqualTo("BL blackToken"),
			() -> assertThat(updatedMember.getRefreshToken()).isEqualTo("logout")
		);
	}

	@Test
	@DisplayName("토큰을 저장한다")
	void saveTokensTest () {
	    // given
		Long memberId = member1.getId();
	    Tokens tokens = new Tokens("accessToken", "refreshToken");

	    // when
		authenticationService.saveTokens(memberId, tokens);
		Member updatedMember = memberRepository.findById(memberId).get();

	    // then
		assertAll(
			() -> assertThat(updatedMember.getAccessToken()).isEqualTo("accessToken"),
			() -> assertThat(updatedMember.getRefreshToken()).isEqualTo("refreshToken")
		);
	}
}