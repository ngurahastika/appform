package com.example.formapp.service;

import java.util.List;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.example.formapp.constants.CacheConstant;
import com.example.formapp.repository.appform.AllowedDomainRepository;
import com.example.formapp.utils.CommonUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class InternalCacheServiceImpl implements InternalCacheService {

	private final CacheManager cacheManager;
	private final AllowedDomainRepository allowedDomainRepository;

	public void saveCache(String cacheName, String key, Object data) {
		if (CommonUtils.isNotEmpty(cacheName) && CommonUtils.isNotEmpty(key) && data != null) {
			this.cacheManager.getCache(cacheName).put(key, data);
		}
	}

	public <T> T getCache(String cacheName, String key, Class<T> type) {
		if (CommonUtils.isNotEmpty(cacheName) && CommonUtils.isNotEmpty(key)) {
			Cache cache = this.cacheManager.getCache(cacheName);
			if (cache != null) {
				return cache.get(key, type);
			}
		}
		return null;
	}

	public void evictCache(String cacheName, String key) {
		if (CommonUtils.isNotEmpty(cacheName) && CommonUtils.isNotEmpty(key)) {
			this.cacheManager.getCache(cacheName).evict(key);
		}
	}

	@Cacheable(value = CacheConstant.ALLOWED_DOMAIN, key = "#idForm")
	public List<String> getAllowedDomain(Long idForm) {
		return this.allowedDomainRepository.getAllByIdForm(idForm);
	}

}
