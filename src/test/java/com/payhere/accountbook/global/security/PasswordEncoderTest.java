package com.payhere.accountbook.global.security;

import static org.assertj.core.api.Assertions.*;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
class PasswordEncoderTest {
	@Autowired
	PasswordEncoder passwordEncoder;

	@Test
	void passwordEncoderTest () {
	    // given
	    String s1 = "qwer1234";
		String s2 = "qwer1234";

	    // when
		String encode1 = passwordEncoder.encode(s1);
		String encode2 = passwordEncoder.encode(s2);

		// then
		assertThat(passwordEncoder.matches(s1, encode2)).isTrue();
	}
}
