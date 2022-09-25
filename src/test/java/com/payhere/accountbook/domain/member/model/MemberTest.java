package com.payhere.accountbook.domain.member.model;

import static org.assertj.core.api.Assertions.*;

import java.text.MessageFormat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.payhere.accountbook.domain.member.util.MemberUtil;
import com.payhere.accountbook.global.error.exception.MemberException;

class MemberTest {
	@ParameterizedTest
	@DisplayName("이메일 형식에 맞지않으면 예외가 발생한다")
	@ValueSource(strings = {"user@gmail", "usergmail", "@gmail.com", "user@.com"})
	void testEmailValidation(String email) {
		assertThatThrownBy(() -> MemberUtil.emailValidation(email))
			.isInstanceOf(MemberException.class)
			.hasMessageContaining(MessageFormat.format("이메일이 유효하지 않습니다. (email: {0})", email));
	}

	@ParameterizedTest
	@DisplayName("비밀번호 형식에 맞지않으면 예외가 발생한다")
	@ValueSource(strings = {"01012345", "dqwdqwdqw", "dqw1", "asdqweqwdqwd12312321312qwdqwdqdq"})
	void testPhoneValidation(String password) {
		assertThatThrownBy(() -> MemberUtil.passwordValidation(password))
			.isInstanceOf(MemberException.class)
			.hasMessageContaining(
				MessageFormat.format("'숫자', '문자' 무조건 1개 이상, 비밀번호 '최소 8자에서 최대 16자'까지 허용 (password: {0})", password));
	}
}