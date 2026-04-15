package com.example.formapp.config;

import java.security.Key;
import java.util.EnumMap;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import com.example.formapp.enums.JwtType;
import com.example.formapp.exception.ProcessingException;

import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "security.authorization")
public class JwtConfig {

	private Map<JwtType, TokenConfig> jwt;
	private final Map<JwtType, Key> keyCache = new EnumMap<>(JwtType.class);

	@Getter
	@Setter
	public static class TokenConfig {
		private String secret;
		private String saltKey;
		private long expired;
	}

	@PostConstruct
	public void init() {
		if (this.jwt.isEmpty()) {
			throw new ProcessingException();
		}
		jwt.forEach((type, config) -> keyCache.put(type, Keys.hmacShaKeyFor(config.getSecret().getBytes())));
	}

	public Key getKey(JwtType type) {
		return keyCache.get(type);
	}

}
