package com.example.formapp.dto;

import java.util.List;

import com.example.formapp.constants.ErrorConstants;
import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FormReq {

	@NotBlank(message = ErrorConstants.ERR_NAME_IS_REQUIRED)
	private String name;

	@NotBlank(message = ErrorConstants.ERR_SLUG_IS_REQUIRED)
	@Pattern(regexp = "^[a-zA-Z0-9.-]+$", message = ErrorConstants.ERR_SLUG_MUST_BE_ALPHANUMERIC)
	private String slug;

	@Size(min = 0, message = ErrorConstants.ERR_ALLOWED_DOMAIN_MUST_BE_ARRAY)
	private List<String> allowed_domains;

	private String description;

	@NotNull(message = ErrorConstants.ERR_LIMIT_RESPONSE_IS_REQUIRED)
	private Boolean limit_one_response;
}
