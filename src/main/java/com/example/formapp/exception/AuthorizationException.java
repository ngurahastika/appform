package com.example.formapp.exception;

import java.util.List;
import java.util.Map;

import com.example.formapp.constants.ErrorConstants;

public class AuthorizationException extends BaseRuntimeException {

	public AuthorizationException() {
		super("401");
		this.message = ErrorConstants.ERR_UNAUTHENTICATED;
	}

	public AuthorizationException(String message) {
		super("401");
		this.message = message;
	}

	public AuthorizationException(String message, Map<String, List<String>> errors) {
		super("401");
		this.message = message;
		this.errors = errors;
	}

}
