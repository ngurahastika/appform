package com.example.formapp.service;

import com.example.formapp.dto.BaseRes;
import com.example.formapp.dto.ResponseReq;
import com.example.formapp.dto.UserProfileDto;

public interface ResponseService {

	public BaseRes submitResponse(UserProfileDto uProfile, String slug, ResponseReq request);

	public BaseRes getAllResponses(UserProfileDto uProfile, String slug);
}
