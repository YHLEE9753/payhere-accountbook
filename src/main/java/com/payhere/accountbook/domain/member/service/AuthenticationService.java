package com.payhere.accountbook.domain.member.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.payhere.accountbook.domain.member.controller.dto.MemberLoginRequest;
import com.payhere.accountbook.domain.member.controller.dto.MemberSignUpRequest;
import com.payhere.accountbook.domain.member.model.Member;
import com.payhere.accountbook.domain.member.repository.MemberRepository;
import com.payhere.accountbook.domain.member.service.dto.MemberResponse;
import com.payhere.accountbook.global.error.exception.MemberException;
import com.payhere.accountbook.global.security.token.Tokens;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
	private final MemberRepository memberRepository;

	@Transactional
	public MemberResponse signupMember(MemberSignUpRequest memberSignUpRequest) {
		checkDuplicatedEmail(memberSignUpRequest.email());
		checkDuplicatedNickName(memberSignUpRequest.nickname());
		Member member = memberRepository.save(MemberConverter.toMember(memberSignUpRequest));

		return MemberConverter.toMemberResponse(member);
	}

	@Transactional(readOnly = true)
	public MemberResponse login(MemberLoginRequest memberLoginRequest) {
		String email = memberLoginRequest.email();
		Member member = memberRepository.findByEmail(email).orElseThrow(() -> {
			throw MemberException.notFoundMemberByEmail(email);
		});
		member.checkPassword(memberLoginRequest.password());

		return MemberConverter.toMemberResponse(member);
	}

	public void logout(Long memberId, String accessTokenWithBlackListType) {
		Member member = memberRepository.findById(memberId).orElseThrow(() -> {
			throw MemberException.notFoundMemberById(memberId);
		});
		member.updateTokens(accessTokenWithBlackListType, "logout");
	}

	@Transactional
	public void saveTokens(Long memberId, Tokens tokens) {
		String accessToken = tokens.accessToken();
		String refreshToken = tokens.refreshToken();
		Member member = memberRepository.findById(memberId).orElseThrow(() -> {
			throw MemberException.notFoundMemberById(memberId);
		});
		member.updateTokens(accessToken, refreshToken);
	}

	private void checkDuplicatedEmail(String email) {
		memberRepository.findByEmail(email).ifPresent(member -> {
			throw MemberException.emailDuplication(email);
		});
	}

	private void checkDuplicatedNickName(String nickname) {
		memberRepository.findByNickname(nickname).ifPresent(member -> {
			throw MemberException.nicknameDuplication(nickname);
		});
	}
}
