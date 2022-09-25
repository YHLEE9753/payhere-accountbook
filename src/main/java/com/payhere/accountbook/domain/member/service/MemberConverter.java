package com.payhere.accountbook.domain.member.service;

import com.payhere.accountbook.domain.member.controller.dto.MemberSignUpRequest;
import com.payhere.accountbook.domain.member.model.Member;
import com.payhere.accountbook.domain.member.service.dto.MemberResponse;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberConverter {
	public static Member toMember(MemberSignUpRequest memberSignUpRequest) {
		return new Member(memberSignUpRequest.email(), memberSignUpRequest.password(), memberSignUpRequest.nickname());
	}

	public static MemberResponse toMemberResponse(Member member) {
		return new MemberResponse(member.getId(), member.getEmail(), member.getNickname());
	}
}
