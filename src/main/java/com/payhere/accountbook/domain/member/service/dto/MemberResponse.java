package com.payhere.accountbook.domain.member.service.dto;

public record MemberResponse (
	Long id,
	String email,
	String nickName
){
}
