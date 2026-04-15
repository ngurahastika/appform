package com.example.formapp.service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.formapp.constants.ErrorConstants;
import com.example.formapp.constants.MessageConstant;
import com.example.formapp.dto.BaseRes;
import com.example.formapp.dto.FormDto;
import com.example.formapp.dto.FormReq;
import com.example.formapp.dto.QuestionDto;
import com.example.formapp.dto.UserProfileDto;
import com.example.formapp.exception.DataNotFoundException;
import com.example.formapp.exception.UnprocessableEntityException;
import com.example.formapp.model.appform.AllowedDomain;
import com.example.formapp.model.appform.Form;
import com.example.formapp.repository.appform.AllowedDomainRepository;
import com.example.formapp.repository.appform.FormRepository;
import com.example.formapp.repository.appform.QuestionsRepository;
import com.example.formapp.utils.DateUtils;
import com.example.formapp.utils.SnowflakeIdGenerator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class FormServiceImpl implements FormService {

	private final SnowflakeIdGenerator snowflakeIdGenerator;
	private final FormRepository formRepository;
	private final AllowedDomainRepository allowedDomainRepository;
	private final QuestionsRepository questionsRepository;
	private final DataFormService dataFormService;
	private final ValidateService validateService;

	public BaseRes<FormDto> createForm(UserProfileDto uProfile, FormReq request) {
		BaseRes<FormDto> response = new BaseRes();
		try {
			Long id = this.formRepository.checkSlug(request.getSlug());
			if (id != null) {
				throw new UnprocessableEntityException(ErrorConstants.ERR_INVALID_FIELD,
						Map.of("slug", List.of(ErrorConstants.ERR_SLUG_ALREADY_TAKEN)));
			}

			Form form = new Form();
			form.setId(snowflakeIdGenerator.nextId());
			form.setName(request.getName());
			form.setSlug(request.getSlug());
			form.setDescription(request.getDescription());
			form.setLimitOneResponse(request.getLimit_one_response());
			form.setIdUser(uProfile.getId());
			form.setCreatedBy(uProfile.getName());
			form.setCreatedDt(DateUtils.getCurrentSQLTimestamp());

			List<AllowedDomain> listAllowedDom = request.getAllowed_domains().stream().map(e -> {
				AllowedDomain allwDom = new AllowedDomain();
				allwDom.setId(snowflakeIdGenerator.nextId());
				allwDom.setIdForm(form.getId());
				allwDom.setDomain(e);
				return allwDom;
			}).toList();

			this.dataFormService.saveAndUpdate(form, listAllowedDom);

			FormDto formDto = new FormDto();
			formDto.setId(form.getId());
			formDto.setName(form.getName());
			formDto.setSlug(form.getSlug());
			formDto.setLimit_one_response(form.isLimitOneResponse());
			formDto.setDescription(form.getDescription());
			formDto.setCreator_id(form.getIdUser());

			response.setMessage(MessageConstant.CREATE_FORM);
			response.setForm(formDto);
		} catch (Exception e) {
			log.error("error : ", e);
			throw e;
		}
		return response;
	}

	public BaseRes listing(UserProfileDto uProfile) {
		BaseRes response = new BaseRes();
		try {

			List<FormDto> listForm = this.formRepository.listingFormByIdUser(uProfile.getId()).stream().map(e -> {
				FormDto formDto = new FormDto();
				formDto.setId(e.getId());
				formDto.setName(e.getName());
				formDto.setSlug(e.getSlug());
				formDto.setLimit_one_response(e.isLimitOneResponse());
				formDto.setDescription(e.getDescription());
				formDto.setCreator_id(e.getIdUser());
				return formDto;
			}).toList();

			response.setMessage(MessageConstant.GET_ALL_FORM);
			response.setForms(listForm);
		} catch (Exception e) {
			log.error("error : ", e);
			throw e;
		}
		return response;
	}

	public BaseRes detail(UserProfileDto uProfile, String slug) {
		BaseRes response = new BaseRes();
		try {

			Optional<Form> formDb = this.formRepository.findBySlug(slug);

			if (formDb.isEmpty()) {
				throw new DataNotFoundException(ErrorConstants.ERR_FORM_NOT_FOUND);
			}

			this.validateService.validateDomain(uProfile.getEmail(), formDb.get().getId());

			FormDto formDto = new FormDto();
			formDto.setId(formDb.get().getId());
			formDto.setName(formDb.get().getName());
			formDto.setSlug(formDb.get().getSlug());
			formDto.setLimit_one_response(formDb.get().isLimitOneResponse());
			formDto.setDescription(formDb.get().getDescription());
			formDto.setCreator_id(formDb.get().getIdUser());

			formDto.setAllowed_domains(this.allowedDomainRepository.getAllByIdForm(formDto.getId()));

			List<QuestionDto> listQuestion = this.questionsRepository.getAllByIdForm(formDto.getId()).stream()
					.map(e -> {
						QuestionDto questionDto = new QuestionDto();
						questionDto.setId(e.getId());
						questionDto.set_required(e.isRequired());
						questionDto.setName(e.getName());
						questionDto.setChoice_type(e.getChoiceType().getLabel());
						questionDto
								.setChoices(e.getChoices() != null ? Arrays.asList(e.getChoices().split(", ")) : null);

						return questionDto;
					}).toList();

			formDto.setQuestions(listQuestion);

			response.setMessage(MessageConstant.GET_FORM_SUCCESS);
			response.setForm(formDto);
		} catch (Exception e) {
			log.error("error : ", e);
			throw e;
		}
		return response;
	}

}
