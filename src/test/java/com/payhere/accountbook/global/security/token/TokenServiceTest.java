package com.payhere.accountbook.global.security.token;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.payhere.accountbook.domain.member.model.MemberRole;

@SpringBootTest
class TokenServiceTest {
	@Autowired
	TokenService tokenService;

	@Test
	@DisplayName("test 용도의 토큰을 생성한다.")
	void makeTestToken () {
		Tokens tokens = tokenService.generateTokens("1", MemberRole.ROLE_MEMBER.name());
		System.out.println("accessToken");
		System.out.println(tokens.accessToken());
		System.out.println("refreshToken");
		System.out.println(tokens.refreshToken());
		System.out.println("======================");
	}
}