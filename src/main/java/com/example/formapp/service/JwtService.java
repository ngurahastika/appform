package com.example.formapp.service;

import java.util.Map;

import com.example.formapp.dto.JwtDto;
import com.example.formapp.enums.JwtType;

public interface JwtService {
	
	public Map<String, Object> claims(JwtType type, String token);
	public String generateToken(JwtType type, Map<String, Object> claims, String subject) ;
	public JwtDto generateTokenDto(JwtType type, Map<String, Object> claims, String subject);
	

}
