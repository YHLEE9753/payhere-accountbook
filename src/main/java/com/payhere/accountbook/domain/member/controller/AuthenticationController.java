package com.payhere.accountbook.domain.member.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.payhere.accountbook.domain.member.controller.dto.MemberLoginRequest;
import com.payhere.accountbook.domain.member.controller.dto.MemberSignUpRequest;
import com.payhere.accountbook.domain.member.model.MemberRole;
import com.payhere.accountbook.domain.member.service.AuthenticationService;
import com.payhere.accountbook.domain.member.service.dto.MemberResponse;
import com.payhere.accountbook.domain.member.service.dto.MemberSignupResponse;
import com.payhere.accountbook.global.security.token.TokenService;
import com.payhere.accountbook.global.security.token.TokenType;
import com.payhere.accountbook.global.security.token.Tokens;
import com.payhere.accountbook.global.util.CoderUtil;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class AuthenticationController {
	private final TokenService tokenService;
	private final AuthenticationService authenticationService;

	@PostMapping("/signup")
	public ResponseEntity<MemberSignupResponse> singUp(
		@Valid @RequestBody MemberSignUpRequest memberSignUpRequest
	) {
		MemberResponse memberResponse = authenticationService.signupMember(memberSignUpRequest);
		Long memberId = memberResponse.id();
		Tokens tokens = tokenService.generateTokens(memberId.toString(), MemberRole.ROLE_MEMBER.name());
		authenticationService.saveTokens(memberId, tokens);
		MemberSignupResponse memberSignupResponse = new MemberSignupResponse(memberResponse,
			CoderUtil.encode(tokens.accessToken()));

		return ResponseEntity
			.ok()
			.body(memberSignupResponse);
	}

	@PostMapping("/login")
	public ResponseEntity<MemberSignupResponse> login(
		@Valid @RequestBody MemberLoginRequest memberLoginRequest
	) {
		MemberResponse memberResponse = authenticationService.login(memberLoginRequest);
		Tokens tokens = tokenService.generateTokens(memberResponse.id().toString(), MemberRole.ROLE_MEMBER.name());
		authenticationService.saveTokens(memberResponse.id(), tokens);

		MemberSignupResponse memberSignupResponse = new MemberSignupResponse(memberResponse,
			CoderUtil.encode(tokens.accessToken()));

		return ResponseEntity
			.ok()
			.body(memberSignupResponse);
	}

	@PostMapping("/logout")
	public ResponseEntity<Void> logout(HttpServletRequest request) {
		String accessToken = tokenService.resolveToken(request);
		String accessTokenWithBlackListType = tokenService.tokenWithType(accessToken, TokenType.JWT_BLACKLIST);
		String memberId = tokenService.getUid(accessToken);
		authenticationService.logout(Long.valueOf(memberId), accessTokenWithBlackListType);

		return ResponseEntity.ok()
			.contentType(MediaType.APPLICATION_JSON)
			.build();
	}
}
