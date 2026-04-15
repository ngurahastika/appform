package com.example.formapp.model.appform;

import com.example.formapp.enums.UserStatus;
import com.example.formapp.model.CreatorMod;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "USR")
@Entity(name = "User")
public class User extends CreatorMod {

	@Id
	@Column(name = "id")
	private Long id;

	@Column(name = "nm")
	private String name;

	@Column(name = "email")
	private String email;

	@Column(name = "status")
	@Enumerated(EnumType.STRING)
	private UserStatus status;

	@Column(name = "pwd")
	private String pwd;

	@Column(name = "login_attemp")
	private int loginAttemp;

}
