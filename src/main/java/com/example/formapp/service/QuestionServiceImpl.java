package com.example.formapp.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.formapp.constants.ErrorConstants;
import com.example.formapp.constants.MessageConstant;
import com.example.formapp.dto.BaseRes;
import com.example.formapp.dto.QuestionDto;
import com.example.formapp.dto.QuestionReq;
import com.example.formapp.dto.UserProfileDto;
import com.example.formapp.enums.ChoiceType;
import com.example.formapp.exception.DataNotFoundException;
import com.example.formapp.exception.UnprocessableEntityException;
import com.example.formapp.model.appform.Questions;
import com.example.formapp.repository.appform.FormRepository;
import com.example.formapp.repository.appform.QuestionsRepository;
import com.example.formapp.utils.DateUtils;
import com.example.formapp.utils.SnowflakeIdGenerator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class QuestionServiceImpl implements QuestionService {

	private final FormRepository formRepository;
	private final QuestionsRepository questionsRepository;
	private final SnowflakeIdGenerator snowflakeIdGenerator;

	public BaseRes addQuestion(UserProfileDto uProfile, String slug, QuestionReq request) {
		BaseRes response = new BaseRes();
		try {
			this.validateChoices(request);

			Long idForm = this.formRepository.findIdForm(slug, uProfile.getId());

			if (idForm == null ) {
				throw new DataNotFoundException(ErrorConstants.ERR_FORM_NOT_FOUND);
			}

			Questions question = new Questions();
			question.setId(snowflakeIdGenerator.nextId());
			question.setName(request.getName());
			question.setChoiceType(request.getChoice_type());
			question.setRequired(request.is_required());
			question.setIdForm(idForm);
			question.setChoices(
					this.isChoiceTypeWithArray(request.getChoice_type()) ? String.join(",", request.getChoices())
							: null);
			question.setCreatedBy(uProfile.getName());
			question.setCreatedDt(DateUtils.getCurrentSQLTimestamp());

			this.questionsRepository.save(question);

			QuestionDto questionDto = new QuestionDto();
			questionDto.setId(question.getId());
			questionDto.setName(question.getName());
			questionDto.setChoice_type(question.getChoiceType().getLabel());
			questionDto.setFormId(idForm);
			questionDto.setId(question.getId());

			response.setQuestion(questionDto);
			response.setMessage(MessageConstant.ADD_QUESTION);

		} catch (Exception e) {
			log.error("error");
			throw e;
		}

		return response;
	}

	@Transactional
	public BaseRes removeQuestion(UserProfileDto uProfile, String slug, String idQuestion) {
		BaseRes response = new BaseRes();
		try {

			int delete = this.questionsRepository.deleteQuestion(Long.valueOf(idQuestion), slug, uProfile.getId());

			if (delete <= 0) {
				throw new DataNotFoundException(ErrorConstants.ERR_QUESTION_NOT_FOUND);
			}

			response.setMessage(MessageConstant.REMOVE_QUESTION);

		} catch (Exception e) {
			log.error("error");
			throw e;
		}

		return response;
	}

	public void validateChoices(QuestionReq request) {
		if (isChoiceTypeWithArray(request.getChoice_type())) {
			if (request.getChoices() == null || request.getChoices().isEmpty()) {
				throw new UnprocessableEntityException(
						Map.of("choices", List.of(ErrorConstants.ERR_CHOICES_IS_REQUIRED)));
			}
		}
	}

	private boolean isChoiceTypeWithArray(ChoiceType type) {
		return type == ChoiceType.MULTIPLE_CHOICE || type == ChoiceType.DROPDOWN || type == ChoiceType.CHECKBOXES;
	}
}
