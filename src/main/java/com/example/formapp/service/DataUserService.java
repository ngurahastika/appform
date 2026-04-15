package com.example.formapp.service;

import com.example.formapp.model.appform.User;
import com.example.formapp.model.appform.UserSession;

public interface DataUserService {

	public void saveAndUpdateSessionUserLogin(User user, UserSession userSession);
	public void updateSessionUserLogin(UserSession userSession);
	public void saveAndUpdateUser(User user);
}
