package com.example.formapp.service;

import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.springframework.stereotype.Service;

import com.example.formapp.config.JwtConfig;
import com.example.formapp.constants.ErrorConstants;
import com.example.formapp.constants.JwtConstant;
import com.example.formapp.dto.JwtDto;
import com.example.formapp.enums.JwtType;
import com.example.formapp.exception.AuthorizationException;
import com.example.formapp.utils.HashUtils;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class JwtServiceImpl implements JwtService {

	private final JwtConfig jwtConfig;

	public Map<String, Object> claims(JwtType type, String token) {

		Map<String, Object> claims = Jwts.parserBuilder().setSigningKey(jwtConfig.getKey(type)).build()
				.parseClaimsJws(token).getBody();

		SortedMap<String, Object> sorted = new TreeMap<>(claims);
		sorted.remove(JwtConstant.SIGN);

		StringBuilder raw = new StringBuilder();
		for (Map.Entry<String, Object> e : sorted.entrySet()) {
			if (!e.getKey().equalsIgnoreCase(JwtConstant.SIGN) && !JwtConstant.IGNORE_KEY.contains(e.getKey())) {
				raw.append(e.getKey()).append(e.getValue());
			}
		}

		raw.append(jwtConfig.getJwt().get(type).getSaltKey());

		String signature = HashUtils.sha256(raw.toString());

		if (!signature.equals(claims.get(JwtConstant.SIGN))) {
			throw new AuthorizationException(ErrorConstants.ERR_AUTH_JWT_EXPIRED);
		}

		return claims;
	}

	public String generateToken(JwtType type, Map<String, Object> claims, String subject) {
		this.createClaims(type, claims);

		return doGenerateToken(claims, subject, jwtConfig.getJwt().get(type).getExpired(), jwtConfig.getKey(type));
	}

	public JwtDto generateTokenDto(JwtType type, Map<String, Object> claims, String subject) {
		this.createClaims(type, claims);
		String token = doGenerateToken(claims, subject, jwtConfig.getJwt().get(type).getExpired(),
				jwtConfig.getKey(type));
		
		
		return new JwtDto(token, Long.parseLong(this.claims(type, token).get(JwtConstant.EXP).toString()) );
	}

	private void createClaims(JwtType type, Map<String, Object> claims) {
		SortedMap<String, Object> sorted = new TreeMap<>(claims);

		StringBuilder raw = new StringBuilder();
		for (Map.Entry<String, Object> e : sorted.entrySet()) {
			raw.append(e.getKey()).append(e.getValue());
		}

		raw.append(jwtConfig.getJwt().get(type).getSaltKey());

		String signature = HashUtils.sha256(raw.toString());
		claims.put(JwtConstant.SIGN, signature);
	}

	private String doGenerateToken(Map<String, Object> claims, String subject, long seconds, Key key) {
		JwtBuilder builder = Jwts.builder().setClaims(claims).setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + seconds * 1000))
				.signWith(key, SignatureAlgorithm.HS512);

		if (subject != null) {
			builder.setSubject(subject);
		}

		return builder.compact();
	}
}
