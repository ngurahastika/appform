package com.example.formapp.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.formapp.constants.ErrorConstants;
import com.example.formapp.constants.MessageConstant;
import com.example.formapp.dto.BaseRes;
import com.example.formapp.dto.ResponseDto;
import com.example.formapp.dto.ResponseReq;
import com.example.formapp.dto.UserDto;
import com.example.formapp.dto.UserProfileDto;
import com.example.formapp.exception.DataNotFoundException;
import com.example.formapp.exception.ForbiddenException;
import com.example.formapp.exception.UnprocessableEntityException;
import com.example.formapp.model.appform.Answers;
import com.example.formapp.model.appform.Form;
import com.example.formapp.model.appform.Responses;
import com.example.formapp.repository.appform.AnswersRepository;
import com.example.formapp.repository.appform.FormRepository;
import com.example.formapp.repository.appform.QuestionsRepository;
import com.example.formapp.repository.appform.ResponsesRepository;
import com.example.formapp.utils.DateUtils;
import com.example.formapp.utils.SnowflakeIdGenerator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class ResponseServiceImpl implements ResponseService {

	private final FormRepository formRepository;
	private final QuestionsRepository questionsRepository;
	private final ResponsesRepository responsesRepository;
	private final AnswersRepository answersRepository;
	private final ValidateService validateService;
	private final SnowflakeIdGenerator snowflakeIdGenerator;
	private final DataResponsesService dataResponsesService;

	public BaseRes submitResponse(UserProfileDto uProfile, String slug, ResponseReq request) {
		BaseRes response = new BaseRes();
		try {

			Form form = this.validateForm(uProfile, slug);

			List<String> idsQuestion = this.questionsRepository.getAllIdByIdForm(form.getId()).stream()
					.map(String::valueOf).toList();

			Responses resp = new Responses();
			resp.setId(snowflakeIdGenerator.nextId());
			resp.setIdForm(form.getId());
			resp.setIdUser(uProfile.getId());
			resp.setCreatedDt(DateUtils.getCurrentSQLTimestamp());
			resp.setCreatedBy(uProfile.getName());

			List<Answers> listAnswer = request.getAnswers().stream().map(e -> {

				if (!idsQuestion.contains(e.getQuestion_id())) {
					throw new UnprocessableEntityException();
				}

				Answers answer = new Answers();
				answer.setId(snowflakeIdGenerator.nextId());
				answer.setIdQuestions(Long.parseLong(e.getQuestion_id()));
				answer.setIdResponse(resp.getId());
				answer.setValue(e.getValue());
				answer.setCreatedBy(uProfile.getName());
				answer.setCreatedDt(DateUtils.getCurrentSQLTimestamp());

				return answer;
			}).toList();

			this.dataResponsesService.saveAndUpdate(resp, listAnswer);

			response.setMessage(MessageConstant.SUBMIT_RESPONSE_SUCCESS);
		} catch (Exception e) {
			log.error("error : ", e);
			throw e;
		}
		return response;
	}

	public BaseRes getAllResponses(UserProfileDto uProfile, String slug) {
		BaseRes response = new BaseRes<>();
		try {

			List<Responses> responses = this.responsesRepository
					.getAllResponseFetchUserByIdUserAndSlug(uProfile.getId(), slug);

			if (responses.isEmpty()) {
				throw new DataNotFoundException(ErrorConstants.ERR_FORM_NOT_FOUND);
			}

			Map<Long, ResponseDto> mapResponse = responses.stream().collect(Collectors.toMap(Responses::getId, res -> {
				UserDto user = new UserDto();
				user.setId(String.valueOf(res.getUser().getId()));
				user.setName(res.getUser().getName());
				user.setEmail(res.getUser().getEmail());

				ResponseDto resDto = new ResponseDto();
				resDto.setDate(DateUtils.formatSQLTimestamp(res.getCreatedDt(), "yyyy-MM-dd HH:mm:ss"));
				resDto.setUser(user);
				return resDto;
			}));

			List<Object[]> listAnswer = this.answersRepository.getQuestionAndAnswersByIdResponse(mapResponse.keySet());

			Map<Long, Map<String, String>> answersGroupedByResponseId = listAnswer.stream()
					.collect(Collectors.groupingBy(answer -> (Long) answer[0],
							Collectors.toMap(answer -> (String) answer[1],
									answer -> answer[2] == null ? "" : (String) answer[2],
									(existing, replacement) -> existing)));

			answersGroupedByResponseId.forEach((responseId, answersMap) -> {
				if (mapResponse.containsKey(responseId)) {
					mapResponse.get(responseId).setAnswers(answersMap);
				}
			});

			response.setResponses(new ArrayList<>(mapResponse.values()));
			response.setMessage("Get responses success");

		} catch (Exception e) {
			log.error("error : ", e);
			throw e;
		}
		return response;
	}

	private Form validateForm(UserProfileDto uProfile, String slug) {
		Optional<Form> formDB = this.formRepository.findBySlug(slug);

		if (formDB.isEmpty()) {
			throw new DataNotFoundException(ErrorConstants.ERR_FORM_NOT_FOUND);
		}

		this.validateService.validateDomain(uProfile.getEmail(), formDB.get().getId());

		if (formDB.get().isLimitOneResponse()) {
			this.validateLimitResponse(uProfile.getId(), formDB.get().getId());
		}
		return formDB.get();
	}

	private void validateLimitResponse(Long idUser, Long IdForm) {
		if (this.responsesRepository.existsByIdUserAndIdForm(idUser, IdForm)) {
			throw new UnprocessableEntityException(ErrorConstants.ERR_CANT_SUBMIT_TWICE);
		}

	}

}
