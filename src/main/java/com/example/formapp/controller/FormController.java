package com.example.formapp.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.formapp.dto.BaseRes;
import com.example.formapp.dto.FormReq;
import com.example.formapp.dto.QuestionReq;
import com.example.formapp.dto.ResponseReq;
import com.example.formapp.dto.UserProfileDto;
import com.example.formapp.service.FormService;
import com.example.formapp.service.QuestionService;
import com.example.formapp.service.ResponseService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/forms")
public class FormController {

	private final FormService formService;
	private final QuestionService questionService;
	private final ResponseService responseService;

	@PostMapping
	public ResponseEntity<BaseRes> createForm(@AuthenticationPrincipal UserProfileDto uProfile,
			@Valid @RequestBody FormReq request) {
		return ResponseEntity.ok(this.formService.createForm(uProfile, request));
	}

	@GetMapping
	public ResponseEntity<BaseRes> listing(@AuthenticationPrincipal UserProfileDto uProfile) {
		return ResponseEntity.ok(this.formService.listing(uProfile));
	}

	@GetMapping("/{slug}")
	public ResponseEntity<BaseRes> detail(@AuthenticationPrincipal UserProfileDto uProfile,
			@PathVariable(name = "slug") String slug) {
		return ResponseEntity.ok(this.formService.detail(uProfile, slug));
	}

	@PostMapping("/{slug}/questions")
	public ResponseEntity<BaseRes> addQuestion(@AuthenticationPrincipal UserProfileDto uProfile,
			@PathVariable(name = "slug") String slug, @Valid @RequestBody QuestionReq request) {
		return ResponseEntity.ok(this.questionService.addQuestion(uProfile, slug, request));
	}

	@DeleteMapping("/{slug}/questions/{idQuestion}")
	public ResponseEntity<BaseRes> deleteQuestion(@AuthenticationPrincipal UserProfileDto uProfile,
			@PathVariable(name = "slug") String slug, @PathVariable(name = "idQuestion") String idQuestion) {
		return ResponseEntity.ok(this.questionService.removeQuestion(uProfile, slug, idQuestion));
	}

	@PostMapping("/{slug}/responses")
	public ResponseEntity<BaseRes> submitResponse(@AuthenticationPrincipal UserProfileDto uProfile,
			@PathVariable(name = "slug") String slug, @Valid @RequestBody ResponseReq request) {
		return ResponseEntity.ok(this.responseService.submitResponse(uProfile, slug, request));
	}

	@GetMapping("{slug}/responses")
	public ResponseEntity<BaseRes> getAllResponse(@AuthenticationPrincipal UserProfileDto uProfile,
			@PathVariable(name = "slug") String slug) {
		return ResponseEntity.ok(this.responseService.getAllResponses(uProfile, slug));
	}

}
