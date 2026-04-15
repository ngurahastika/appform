package com.example.formapp.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.formapp.dto.BaseRes;
import com.example.formapp.dto.LoginReq;
import com.example.formapp.dto.UserProfileDto;
import com.example.formapp.service.AuthService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

	private final AuthService authService;

	@PostMapping("/login")
	ResponseEntity<BaseRes> login(@Valid @RequestBody LoginReq request) {
		return ResponseEntity.ok(this.authService.login(request));
	}

	@PostMapping("/logout")
	ResponseEntity<BaseRes> logout(@AuthenticationPrincipal UserProfileDto uProfile) {
		return ResponseEntity.ok(this.authService.logout(uProfile));
	}
	
	@PostMapping("/refresh/token")
	ResponseEntity<BaseRes> refreshToken(@AuthenticationPrincipal UserProfileDto uProfile) {
		return ResponseEntity.ok(this.authService.refreshToken(uProfile));
	}

}
