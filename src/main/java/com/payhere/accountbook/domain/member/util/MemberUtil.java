package com.payhere.accountbook.domain.member.util;

import java.util.regex.Pattern;

import com.payhere.accountbook.global.error.exception.MemberException;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberUtil {
	private static final String EMAIL_REGEX = "[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?";
	private static final String PASSWORD_REGEX = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,16}$";

	public static void emailValidation (String email) {
		boolean matches = Pattern.matches(EMAIL_REGEX, email);
		if (!matches) {
			throw MemberException.invalidEmail(email);
		}
	}

	public static void passwordValidation (String password) {
		boolean matches = Pattern.matches(PASSWORD_REGEX, password);
		if (!matches) {
			throw MemberException.invalidPassword(password);
		}
	}
}
