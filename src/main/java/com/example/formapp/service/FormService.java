package com.example.formapp.service;

import com.example.formapp.dto.BaseRes;
import com.example.formapp.dto.FormDto;
import com.example.formapp.dto.FormReq;
import com.example.formapp.dto.UserProfileDto;

public interface FormService {
	
	public BaseRes<FormDto> createForm(UserProfileDto uProfile, FormReq request);
	public BaseRes listing(UserProfileDto uProfile);
	public BaseRes detail(UserProfileDto uProfile, String slug);
}
