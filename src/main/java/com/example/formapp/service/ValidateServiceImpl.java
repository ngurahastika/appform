package com.example.formapp.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.formapp.exception.ForbiddenException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class ValidateServiceImpl implements ValidateService {
	
	private final InternalCacheService internalCacheService;
	
	public void validateDomain(String email, Long idForm) {
		List<String> allowedDomains = this.internalCacheService.getAllowedDomain(idForm);
		if (allowedDomains == null || allowedDomains.isEmpty()) {
			return;
		}
		String userDomain = email.substring(email.indexOf("@") + 1);

		boolean isAllowed = allowedDomains.stream().anyMatch(domain -> domain.equalsIgnoreCase(userDomain));

		if (!isAllowed) {
			throw new ForbiddenException();
		}

	}

}
