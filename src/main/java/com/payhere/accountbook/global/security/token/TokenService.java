package com.payhere.accountbook.global.security.token;

import static org.springframework.http.HttpHeaders.*;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.payhere.accountbook.global.error.exception.TokenException;
import com.payhere.accountbook.global.util.CoderUtil;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Service
public class TokenService {
	private final String issuer;
	private final long tokenPeriod;
	private final long refreshPeriod;
	private final Key key;
	private String role = "roles";

	public TokenService(JwtProperties jwtProperties) {
		this.issuer = jwtProperties.getIssuer();
		this.tokenPeriod = jwtProperties.getTokenExpiry();
		this.refreshPeriod = jwtProperties.getRefreshTokenExpiry();
		this.key = Keys.hmacShaKeyFor(
			Base64.getEncoder().encodeToString(jwtProperties.getTokenSecret().getBytes()).getBytes());
	}

	public Tokens generateTokens(String uid, String roles) {
		Claims claims = Jwts.claims().setSubject(uid);
		claims.put(role, roles);
		Date now = new Date();

		return new Tokens(generateAccessToken(claims, now), generateRefreshToken(claims, now));
	}

	public String generateAccessToken(Claims claims, Date now) {
		return Jwts.builder()
			.setIssuer(issuer)
			.setClaims(claims)
			.setIssuedAt(now)
			.setExpiration(new Date(now.getTime() + tokenPeriod))
			.signWith(key, SignatureAlgorithm.HS256)
			.compact();
	}

	public String generateAccessToken(String uid, String[] roles) {
		Claims claims = Jwts.claims().setSubject(uid);
		claims.put(role, roles[0]);
		Date now = new Date();

		return this.generateAccessToken(claims, now);
	}

	public String generateRefreshToken(Claims claims, Date now) {
		return Jwts.builder()
			.setIssuer(issuer)
			.setClaims(claims)
			.setIssuedAt(now)
			.setExpiration(new Date(now.getTime() + refreshPeriod))
			.signWith(key, SignatureAlgorithm.HS256)
			.compact();
	}

	public String generateRefreshToken(Long uid, String[] roles, Date now) {
		Claims claims = Jwts.claims().setSubject(String.valueOf(uid));
		claims.put(role, roles[0]);
		return Jwts.builder()
			.setIssuer(issuer)
			.setClaims(claims)
			.setIssuedAt(now)
			.setExpiration(new Date(now.getTime() + refreshPeriod))
			.signWith(key, SignatureAlgorithm.HS256)
			.compact();
	}

	public boolean verifyToken(String token) {
		try {
			Jws<Claims> claims = Jwts.parserBuilder()
				.setSigningKey(key)
				.build()
				.parseClaimsJws(token);

			return claims.getBody()
				.getExpiration()
				.after(new Date());
		} catch (Exception e) {
			return false;
		}
	}

	public String[] getRole(String token) {
		return new String[] {
			(String)Jwts.parserBuilder()
				.setSigningKey(key)
				.build()
				.parseClaimsJws(token)
				.getBody()
				.get(role)
		};
	}

	public String getUid(String token) {
		return Jwts.parserBuilder()
			.setSigningKey(key)
			.build()
			.parseClaimsJws(token)
			.getBody()
			.getSubject();
	}

	public long getExpiration(String token) {
		Date expiration = Jwts.parserBuilder()
			.setSigningKey(key)
			.build()
			.parseClaimsJws(token)
			.getBody()
			.getExpiration();
		long now = new Date().getTime();

		return (expiration.getTime() - now);
	}

	public String changeToToken(String header) {
		return header.substring("Bearer ".length());
	}

	public long getRefreshTokenPeriod() {
		return refreshPeriod;
	}

	public String tokenWithType(String accessToken, TokenType tokenType) {
		return tokenType.getTypeValue() + accessToken;
	}

	public String resolveToken(HttpServletRequest request) {
		Optional<String> tokenHeader = Optional.ofNullable(request.getHeader(AUTHORIZATION));
		String token = tokenHeader.map(this::changeToToken).orElse(null);

		return token != null ? CoderUtil.decode(token) : null;
	}

	public void verifyAccessTokenWithException(String accessToken) {
		if (!this.verifyToken(accessToken)) {
			throw TokenException.accessTokenExpiration(accessToken);
		}
	}

	public void verifyRefreshTokenWithException(String refreshTokenValue) {
		if (!this.verifyToken(refreshTokenValue)) {
			throw TokenException.refreshTokenExpiration(refreshTokenValue);
		}
	}

	public boolean isBlackList(String accessToken) {
		return accessToken.split(" ")[0].equals("BL");
	}
}
