package com.example.formapp.repository.appform;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.formapp.model.appform.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
	
	@Query(value = "FROM User u WHERE u.email = :email ",nativeQuery = false)
	Optional<User> findByEmail(String email);

}
