package com.example.formapp.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FormDto {

	@JsonSerialize(using = ToStringSerializer.class)
	private Long id;
	
	@JsonSerialize(using = ToStringSerializer.class)
	private Long creator_id;
	private String name;
	private String slug;
	private List<String> allowed_domains;
	private String description;
	private Boolean limit_one_response;

	List<QuestionDto> questions;
	
}
