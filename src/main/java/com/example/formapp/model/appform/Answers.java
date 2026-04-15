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
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "answers")
@Entity(name = "Answers")
public class Answers {

	@Id
	@Column(name = "ID")
	private Long id;

	@Column(name = "ID_RESPONSE")
	private Long idResponse;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_RESPONSE", insertable = false, updatable = false)
	private Responses responses;

	@Column(name = "ID_QUESTION")
	private Long idQuestions;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_QUESTION", insertable = false, updatable = false)
	private Questions questions;
	
	@Column(name = "VALUE")
	private String value;

	@Version
	@Column(name = "VERSION")
	private Long version;

	@Column(name = "CREATED_BY")
	private String createdBy;

	@Column(name = "CREATED_DT")
	private Timestamp createdDt;

}
