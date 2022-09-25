package com.payhere.accountbook.global.error.exception;

import java.text.MessageFormat;

import com.payhere.accountbook.global.error.dto.ErrorCode;

public class MemberException extends BusinessException {

	protected MemberException(ErrorCode errorCode, String message) {
		super(errorCode, message);
	}

	protected MemberException(ErrorCode errorCode, String message, Throwable cause) {
		super(errorCode, message, cause);
	}

	public static MemberException notFoundMemberById(Long memberId) {
		return new MemberException(ErrorCode.NOT_FOUND_MEMBER_BY_ID,
			MessageFormat.format("회원을 id로 찾을 수 없습니다. (id: {0})", memberId));
	}

	public static MemberException notFoundMemberByEmail(String email) {
		return new MemberException(ErrorCode.NOT_FOUND_MEMBER_BY_EMAIL,
			MessageFormat.format("회원을 email로 찾을 수 없습니다. (email: {0})", email));
	}

	public static MemberException emailDuplication(String email) {
		return new MemberException(ErrorCode.EMAIL_DUPLICATED_MEMBER,
			MessageFormat.format("이미 등록된 회원 입니다. (email: {0})", email));
	}

	public static MemberException nicknameDuplication(String nickname) {
		return new MemberException(ErrorCode.NICKNAME_DUPLICATED_MEMBER,
			MessageFormat.format("이미 등록된 회원 입니다. (nickname: {0})", nickname));
	}

	public static MemberException invalidPassword() {
		return new MemberException(ErrorCode.INVALID_PASSWORD, "비밀번호가 유효하지 않습니다.");
	}

	public static MemberException blackTypeTokenDetection() {
		return new MemberException(ErrorCode.BlACK_TYPE_TOKEN_DETECTION, "로그아웃처리된 토큰으로 접속이 감지되었습니다");
	}

	public static MemberException notFoundMemberByAccessToken(String accessToken) {
		return new MemberException(ErrorCode.NOT_FOUND_MEMBER_BY_ACCESS_TOKEN,
			MessageFormat.format("회원을 access token으로 찾을 수 없습니다. (token: {0})", accessToken));
	}
}

