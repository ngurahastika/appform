package com.example.formapp.repository.appform;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.formapp.model.appform.UserSession;

public interface UserSessionRepository extends JpaRepository<UserSession, Long> {
	
	@Query(value = "SELECT us.sid FROM UserSession us WHERE us.id = :idUser ",nativeQuery =  false)
	public String getSession(Long idUser);

}
