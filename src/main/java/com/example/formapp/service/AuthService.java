package com.example.formapp.service;

import java.util.Map;

import org.springframework.security.core.Authentication;

import com.example.formapp.dto.BaseRes;
import com.example.formapp.dto.LoginReq;
import com.example.formapp.dto.UserProfileDto;

import io.jsonwebtoken.JwtException;

public interface AuthService {

	public Authentication validateAuthJwt(Map<String, String> datas) throws JwtException;

	public BaseRes login(LoginReq request);

	public BaseRes logout(UserProfileDto uProfile);

	public BaseRes refreshToken(UserProfileDto uProfile);
}
