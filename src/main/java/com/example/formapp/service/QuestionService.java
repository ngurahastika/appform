package com.example.formapp.service;

import com.example.formapp.dto.BaseRes;
import com.example.formapp.dto.QuestionReq;
import com.example.formapp.dto.UserProfileDto;

public interface QuestionService {

	public BaseRes addQuestion(UserProfileDto uProfile, String slug, QuestionReq request);

	public BaseRes removeQuestion(UserProfileDto uProfile, String slug, String idQuestion);
}
