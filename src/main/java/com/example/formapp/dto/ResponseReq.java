package com.example.formapp.dto;

import java.util.List;

import com.example.formapp.constants.ErrorConstants;
import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseReq {

	@NotEmpty(message = ErrorConstants.ERR_ANSWER_CANT_EMPTY)
	private List<AnswerDto> answers;
}
