package com.example.formapp.enums;

import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ChoiceType {
	@JsonProperty("short answer")
	SHORT_ANSWER("short answer"),

	@JsonProperty("paragraph")
	PARAGRAPH("paragraph"),

	@JsonProperty("date")
	DATE("date"),

	@JsonProperty("multiple choice")
	MULTIPLE_CHOICE("multiple choice"),

	@JsonProperty("dropdown")
	DROPDOWN("dropdown"),

	@JsonProperty("checkboxes")
	CHECKBOXES("checkboxes");

	private final String label;

	ChoiceType(String label) {
		this.label = label;
	}

	@JsonValue
	public String getLabel() {
		return label;
	}

	@JsonCreator
	public static ChoiceType fromString(String value) {
		if (value == null || value.trim().isEmpty()) {
			return null; 
		}

		return Arrays.stream(ChoiceType.values())
				.filter(type -> type.label.equalsIgnoreCase(value) || type.name().equalsIgnoreCase(value)).findFirst()
				.orElse(null); 
	}
}
