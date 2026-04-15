package com.example.formapp.model.appform;

import com.example.formapp.model.CreatorMod;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "USR_SESSION")
@Entity(name = "UserSession")
public class UserSession extends CreatorMod {

	@Id
	@Column(name = "ID")
	private Long id;

	@Column(name = "SID")
	private String sid;

	@Column(name = "REFRESH_COUNT")
	private int refreshCount;

}
