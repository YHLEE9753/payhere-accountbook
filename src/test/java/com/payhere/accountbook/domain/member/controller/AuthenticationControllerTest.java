package com.payhere.accountbook.domain.member.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import com.payhere.accountbook.config.ControllerTestConfig;
import com.payhere.accountbook.domain.member.controller.dto.MemberLoginRequest;
import com.payhere.accountbook.domain.member.controller.dto.MemberSignUpRequest;
import com.payhere.accountbook.domain.member.model.MemberRole;
import com.payhere.accountbook.domain.member.service.AuthenticationService;
import com.payhere.accountbook.domain.member.service.dto.MemberLoginResponse;
import com.payhere.accountbook.domain.member.service.dto.MemberResponse;
import com.payhere.accountbook.domain.member.service.dto.MemberSignupResponse;
import com.payhere.accountbook.global.security.token.TokenService;
import com.payhere.accountbook.global.security.token.TokenType;
import com.payhere.accountbook.global.security.token.Tokens;

@WebMvcTest(controllers = AuthenticationController.class)
class AuthenticationControllerTest extends ControllerTestConfig {
	@MockBean
	TokenService tokenService;

	@MockBean
	AuthenticationService authenticationService;

	@Test
	@DisplayName("/api/v1/signup 에서 회원가입한다")
	void postMemberSignup() throws Exception {
		// given
		MemberSignUpRequest memberSignUpRequest = new MemberSignUpRequest("test123@google.com", "whale123",
			"qwer1234!@#$");
		MemberResponse memberResponse = new MemberResponse(1L, "test123@google.com", "whale123");
		String accessToken = "accessToken";
		MemberSignupResponse memberSignupResponse = new MemberSignupResponse(memberResponse, accessToken);

		Tokens tokens = new Tokens("accessToken", "RefreshToken");

		given(authenticationService.signupMember(memberSignUpRequest)).willReturn(memberResponse);
		given(tokenService.generateTokens(memberResponse.id().toString(), MemberRole.ROLE_MEMBER.name()))
			.willReturn(tokens);

		// when
		ResultActions resultActions = mockMvc.perform(
			post("/api/v1/signup")
				.content(objectMapper.writeValueAsString(memberSignUpRequest))
				.contentType(MediaType.APPLICATION_JSON));

		// then
		resultActions
			.andExpectAll(status().isOk(),
				content().json(objectMapper.writeValueAsString(memberSignupResponse)))
			.andDo(print())
			.andDo(document(COMMON_DOCS_NAME,
				requestHeaders(
					headerWithName(HttpHeaders.CONTENT_TYPE).description("json 으로 전달")
				),
				requestFields(
					fieldWithPath("email").type(STRING).description("이메일"),
					fieldWithPath("nickname").type(STRING).description("닉네임"),
					fieldWithPath("password").type(STRING).description("비밀번호")
				),
				responseHeaders(
					headerWithName(HttpHeaders.CONTENT_TYPE).description("json 으로 전달")
				),
				responseFields(
					fieldWithPath("memberResponse").type(OBJECT).description("멤버"),
					fieldWithPath("memberResponse.id").type(NUMBER).description("아이디"),
					fieldWithPath("memberResponse.email").type(STRING).description("이메일"),
					fieldWithPath("memberResponse.nickName").type(STRING).description("닉네임"),
					fieldWithPath("accessToken").type(STRING).description("access 토큰값")
				)));
	}

	@Test
	@DisplayName("/api/v1/login 에서 로그인을 한다")
	void postLogin() throws Exception {
		// given
		MemberLoginRequest memberLoginRequest = new MemberLoginRequest("test123@google.com", "qwer1234!@#$");
		MemberResponse memberResponse = new MemberResponse(1L, "test123@google.com", "whale123");
		Tokens tokens = new Tokens("accessToken", "RefreshToken");
		String accessToken = "accessToken";
		MemberLoginResponse memberLoginResponse = new MemberLoginResponse(memberResponse, accessToken);
		given(tokenService.generateTokens(memberResponse.id().toString(), MemberRole.ROLE_MEMBER.name()))
			.willReturn(tokens);
		given(authenticationService.login(memberLoginRequest)).willReturn(memberResponse);

		// when
		ResultActions resultActions = mockMvc.perform(
			post("/api/v1/login")
				.content(objectMapper.writeValueAsString(memberLoginRequest))
				.contentType(MediaType.APPLICATION_JSON));

		// then
		resultActions
			.andExpectAll(status().isOk(),
				content().json(objectMapper.writeValueAsString(memberLoginResponse)))
			.andDo(print())
			.andDo(document(COMMON_DOCS_NAME,
				requestHeaders(
					headerWithName(HttpHeaders.CONTENT_TYPE).description("json 으로 전달")
				),
				requestFields(
					fieldWithPath("email").type(STRING).description("이메일"),
					fieldWithPath("password").type(STRING).description("비밀번호")
				),
				responseHeaders(
					headerWithName(HttpHeaders.CONTENT_TYPE).description("json 으로 전달")
				),
				responseFields(
					fieldWithPath("memberResponse").type(OBJECT).description("멤버"),
					fieldWithPath("memberResponse.id").type(NUMBER).description("아이디"),
					fieldWithPath("memberResponse.email").type(STRING).description("이메일"),
					fieldWithPath("memberResponse.nickName").type(STRING).description("닉네임"),
					fieldWithPath("accessToken").type(STRING).description("access 토큰값")
				)));
	}

	@Test
	@DisplayName("/api/v1/logout 에서 로그아웃을 한다")
	void postLogout() throws Exception {
		// given
		String accessToken = "accessToken";
		String accessTokenWithType = "accessTokenWithType";
		String memberId = "1";

		given(tokenService.resolveToken(any())).willReturn(accessToken);
		given(tokenService.tokenWithType(accessToken, TokenType.JWT_BLACKLIST)).willReturn(accessTokenWithType);
		given(tokenService.getUid(any())).willReturn(memberId);

		// when // then
		ResultActions resultActions = mockMvc.perform(
			post("/api/v1/logout")
				.contentType(MediaType.APPLICATION_JSON));

		resultActions
			.andExpect(status().isOk())
			.andDo(print())
			.andDo(document(COMMON_DOCS_NAME));
	}
}