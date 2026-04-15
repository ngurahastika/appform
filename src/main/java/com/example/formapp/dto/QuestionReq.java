package com.example.formapp.dto;

import java.util.List;

import com.example.formapp.constants.ErrorConstants;
import com.example.formapp.enums.ChoiceType;
import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class QuestionReq {

	@NotBlank(message = ErrorConstants.ERR_NAME_IS_REQUIRED)
	private String name;

	@NotNull(message = ErrorConstants.ERR_CHOICE_TYPE_IS_REQUIRED)
	private ChoiceType choice_type;

	private List<String> choices;

	private boolean is_required;
}
