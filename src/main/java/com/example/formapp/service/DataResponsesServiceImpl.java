package com.example.formapp.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.formapp.model.appform.Answers;
import com.example.formapp.model.appform.Responses;
import com.example.formapp.repository.appform.AnswersRepository;
import com.example.formapp.repository.appform.ResponsesRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class DataResponsesServiceImpl implements DataResponsesService {

	private final AnswersRepository answersRepository;
	private final ResponsesRepository responsesRepository;

	@Transactional
	public void saveAndUpdate(Responses responses, List<Answers> answers) {
		this.responsesRepository.save(responses);
		this.answersRepository.saveAll(answers);
	}

}
