package com.payhere.accountbook.domain.member.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.payhere.accountbook.domain.member.util.MemberUtil;
import com.payhere.accountbook.global.error.exception.MemberException;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "member_id", unique = true, nullable = false, updatable = false)
	private Long id;

	@Column(name = "email", length = 320, unique = true, nullable = false)
	private String email;

	@Column(name = "password", length = 16, nullable = false)
	private String password;

	@Column(name = "nickname", length = 10, unique = true, nullable = false)
	private String nickname;

	@Enumerated(value = EnumType.STRING)
	@Column(name = "member_role", length = 16, nullable = false)
	private MemberRole memberRole;

	@Column(name = "refresh_token", length = 40, unique = true)
	private String refreshToken;

	@Column(name = "access_token", length = 40, unique = true)
	private String accessToken;

	public Member(String email, String password, String nickname) {
		MemberUtil.emailValidation(email);
		MemberUtil.passwordValidation(password);
		this.email = email;
		this.password = password;
		this.nickname = nickname;
		this.memberRole = MemberRole.ROLE_MEMBER;
	}

	public void updateRefreshToken(String refreshToken){
		this.refreshToken = refreshToken;
	}

	public void updateTokens(String accessToken, String refreshToken){
		this.accessToken = accessToken;
		this.refreshToken = refreshToken;
	}

	public void checkPassword(String password) {
		if(!this.password.equals(password)){
			throw MemberException.invalidPassword();
		}
	}
}
