package com.example.formapp.service;

import java.util.List;

public interface InternalCacheService {

	public void saveCache(String cacheName, String key, Object data);

	public <T> T getCache(String cacheName, String key, Class<T> type);

	public void evictCache(String cacheName, String key);
	
	public List<String> getAllowedDomain(Long idForm);

}
