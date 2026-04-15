package com.example.formapp.model;

import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@MappedSuperclass
public class CreatorMod {
	
	@Column(name = "CREATED_BY")
	private String createdBy;

	@Column(name = "CREATED_DT")
	private Timestamp createdDt;
	
	@Column(name = "UPDATED_BY")
	private String updatedBy;

	@Column(name = "UPDATED_DT")
	private Timestamp updatedDt;
	
}
