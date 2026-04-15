package com.example.formapp.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.validation.constraints.Email;
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
public class LoginReq {

	@Email(message = "The email must be a valid email address")
    @NotEmpty(message = "The email field is required.")
	private String email;
	
	@NotEmpty(message = "The password field is required.")
	private String password;
}
