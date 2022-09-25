package com.payhere.accountbook.global.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.payhere.accountbook.domain.member.model.Member;
import com.payhere.accountbook.domain.member.repository.MemberRepository;
import com.payhere.accountbook.global.error.dto.ErrorCode;
import com.payhere.accountbook.global.error.dto.TokenExpirationResponse;
import com.payhere.accountbook.global.error.exception.MemberException;
import com.payhere.accountbook.global.security.token.TokenService;
import com.payhere.accountbook.global.util.CoderUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
	private final TokenService tokenService;
	private final MemberRepository memberRepository;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {

		String accessToken = tokenService.resolveToken(request);
		boolean isLogout = request.getServletPath().equals("/api/v1/logout");
		ObjectMapper objectMapper = new ObjectMapper();

		if (!isLogout && isAccessTokenValid(accessToken)) {
			// 1. accessToken, RefreshToken 모두 유효한 경우 - 정상처리
			Long memberId = Long.parseLong(tokenService.getUid(accessToken));
			String[] roles = tokenService.getRole(accessToken);

			Member member = memberRepository.findById(memberId).orElseThrow(() -> {
				throw MemberException.notFoundMemberById(memberId);
			});

			if(tokenService.isBlackList(member.getAccessToken())){
				log.warn("JWT Attack is detected");
				throw MemberException.blackTypeTokenDetection();
			}

			// 2. accessToken 은 유효하지만, refreshToken 이 만료 -> accessToken 을 검증하여 refreshToken 재발급
			if (!isRefreshTokenValid(member.getRefreshToken())) {
				String refreshToken = tokenService.generateRefreshToken(memberId, roles, new Date());
				member.updateRefreshToken(refreshToken);
			}

			setAuthenticationToSecurityContextHolder(memberId, roles);
			filterChain.doFilter(request, response);
			return;
		} else if (!isLogout && accessToken != null) {
			// 3. accessToken 만료, refreshToken 유효 -> refreshToken 을 검증하여 accessToken 재발급
			Member member = memberRepository.findByAccessTokenValue(accessToken).orElseThrow(() -> {
				throw MemberException.notFoundMemberByAccessToken(accessToken);
			});
			String refreshTokenValue = member.getRefreshToken();

			if (isRefreshTokenValid(refreshTokenValue)) {
				String[] roles = tokenService.getRole(refreshTokenValue);
				String accessTokenValue = tokenService.generateAccessToken(String.valueOf(member.getId()), roles);

				accessTokenExpirationException(response, objectMapper, CoderUtil.encode(accessTokenValue));
				return;
			}

			// 4. accessToken 과 refreshToken 모두 만료 - 에러 발생
			refreshTokenExpirationException(response, objectMapper);
			return;
		}

		filterChain.doFilter(request, response);
	}

	private boolean isRefreshTokenValid(String refreshTokenValue) {
		return tokenService.verifyToken(refreshTokenValue);
	}

	private boolean isAccessTokenValid(String accessToken) {
		return accessToken != null && tokenService.verifyToken(accessToken);
	}

	private void accessTokenExpirationException(HttpServletResponse response, ObjectMapper objectMapper,
		String newAccessToken) throws IOException {
		response.setStatus(HttpStatus.UNAUTHORIZED.value());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setCharacterEncoding("UTF-8");
		objectMapper.writeValue(response.getWriter(),
			TokenExpirationResponse.of(ErrorCode.ACCESS_TOKEN_EXPIRATION, newAccessToken));
	}

	private void refreshTokenExpirationException(HttpServletResponse response, ObjectMapper objectMapper) throws
		IOException {
		response.setStatus(HttpStatus.UNAUTHORIZED.value());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setCharacterEncoding("UTF-8");
		objectMapper.writeValue(response.getWriter(), TokenExpirationResponse.of(ErrorCode.REFRESH_TOKEN_EXPIRATION));
	}

	private void setAuthenticationToSecurityContextHolder(Long memberId, String[] roles) {
		Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
		Arrays.stream(roles).forEach(role -> authorities.add(new SimpleGrantedAuthority(role)));

		UsernamePasswordAuthenticationToken authenticationToken =
			new UsernamePasswordAuthenticationToken(memberId, null, authorities);

		SecurityContextHolder.getContext().setAuthentication(authenticationToken);
	}
}