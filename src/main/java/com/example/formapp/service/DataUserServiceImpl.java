package com.example.formapp.service;

import org.springframework.stereotype.Service;

import com.example.formapp.model.appform.User;
import com.example.formapp.model.appform.UserSession;
import com.example.formapp.repository.appform.UserRepository;
import com.example.formapp.repository.appform.UserSessionRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class DataUserServiceImpl implements DataUserService {

	private final UserRepository userRepository;
	private final UserSessionRepository userSessionRepository;

	@Transactional
	public void saveAndUpdateSessionUserLogin(User user, UserSession userSession) {
		this.userRepository.save(user);
		this.userSessionRepository.save(userSession);
	}

	@Transactional
	public void updateSessionUserLogin(UserSession userSession) {
		this.userSessionRepository.save(userSession);
	}

	@Transactional
	public void saveAndUpdateUser(User user) {
		this.userRepository.save(user);
	}

}
