package com.example.formapp.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.formapp.model.appform.AllowedDomain;
import com.example.formapp.model.appform.Form;
import com.example.formapp.repository.appform.AllowedDomainRepository;
import com.example.formapp.repository.appform.FormRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class DataFormServiceImpl implements DataFormService{

	private final FormRepository formRepository;
	private final AllowedDomainRepository allowedDomainRepository;

	@Transactional
	public void saveAndUpdate(Form form, List<AllowedDomain> allowedDomain) {
		this.formRepository.save(form);
		this.allowedDomainRepository.saveAll(allowedDomain);
	}
}
