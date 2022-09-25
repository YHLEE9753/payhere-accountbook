package com.payhere.accountbook.domain.member.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AccessLevel;
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

	@Column(name = "email", length = 40, unique = true, nullable = false)
	private String email;

	@Column(name = "password", length = 40, unique = true, nullable = false)
	private String password;


	@Column(name = "refresh_token_value", length = 40, unique = true)
	private String refreshTokenValue;

	@Column(name = "access_token_value", length = 40, unique = true)
	private String accessTokenValue;

	public void updateRefreshToken(String refreshTokenValue){
		this.refreshTokenValue = refreshTokenValue;
	}
}
