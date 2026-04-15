package com.example.formapp.config;

import java.util.concurrent.TimeUnit;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.example.formapp.constants.CacheConstant;
import com.github.benmanes.caffeine.cache.Caffeine;

import lombok.Getter;
import lombok.Setter;

@Configuration
@ConfigurationProperties(prefix = "cache.config.internal")
@Setter
@Getter
@EnableCaching
public class InternalCacheConfig {

	private CacheProps session;
	private CacheProps uprofile;
	private CacheProps allowedDomain;

	@Getter
	@Setter
	public static class CacheProps {
		private Integer expired;
		private Integer size;
		private Integer capacity;
	}

	@Primary
	@Bean("caffeineCacheManager")
	public CacheManager caffeineCacheManager() {
		CaffeineCacheManager cacheManager = new CaffeineCacheManager();

		cacheManager.registerCustomCache(CacheConstant.PROFILE_USER,
				Caffeine.newBuilder().initialCapacity(uprofile.getCapacity()).maximumSize(uprofile.getSize())
						.expireAfterWrite(uprofile.getExpired(), TimeUnit.MINUTES).build());

		cacheManager.registerCustomCache(CacheConstant.SESSION_USER,
				Caffeine.newBuilder().initialCapacity(session.getCapacity()).maximumSize(session.getSize())
						.expireAfterWrite(session.getExpired(), TimeUnit.MINUTES).build());
		
		cacheManager.registerCustomCache(CacheConstant.ALLOWED_DOMAIN,
				Caffeine.newBuilder().initialCapacity(allowedDomain.getCapacity()).maximumSize(allowedDomain.getSize())
						.expireAfterWrite(allowedDomain.getExpired(), TimeUnit.MINUTES).build());

		return cacheManager;
	}
}
