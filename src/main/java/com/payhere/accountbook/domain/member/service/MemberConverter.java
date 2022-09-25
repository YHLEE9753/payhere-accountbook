package com.payhere.accountbook.domain.member.service;



import com.payhere.accountbook.domain.member.controller.dto.MemberSignUpRequest;
import com.payhere.accountbook.domain.member.model.Member;
import com.payhere.accountbook.domain.member.service.dto.MemberResponse;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberConverter {
	public static Member toMember(MemberSignUpRequest memberSignUpRequest){
		return Member.builder()
			.email(memberSignUpRequest.email())
			.nickname(memberSignUpRequest.nickname())
			.password(memberSignUpRequest.password())
			.build();
	}

	public static MemberResponse toMemberResponse(Member member) {
		return MemberResponse.builder()
			.id(member.getId())
			.email(member.getEmail())
			.nickName(member.getNickname())
			.build();
	}
}
