package com.example.formapp.dto;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseRes<T> {

	private String reqId;
	private String message;
	
	private T user;
    private T form;
    private T forms;
    private T question;
    private T questions;
    private T responses;
    private Map<String, Object> errors;
}
