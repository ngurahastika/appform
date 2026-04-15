package com.example.formapp.model.appform;

import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "responses")
@Entity(name = "Responses")
public class Responses {

	@Id
	@Column(name = "ID")
	private Long id;

	@Column(name = "ID_USR")
	private Long idUser;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_USR", insertable = false, updatable = false)
	private User user;

	@Column(name = "ID_FORM")
	private Long idForm;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_FORM", insertable = false, updatable = false)
	private Form form;

	@Version
	@Column(name = "VERSION")
	private Long version;

	@Column(name = "CREATED_BY")
	private String createdBy;

	@Column(name = "CREATED_DT")
	private Timestamp createdDt;

}
