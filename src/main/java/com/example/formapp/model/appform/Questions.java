package com.example.formapp.model.appform;

import com.example.formapp.enums.ChoiceType;
import com.example.formapp.model.CreatorMod;

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
@Table(name = "questions")
@Entity(name = "Questions")
public class Questions extends CreatorMod {

	@Id
	@Column(name = "ID")
	private Long id;

	@Column(name = "NM")
	private String name;

	@Column(name = "CHOICE_TYPE")
	private ChoiceType choiceType;

	@Column(name = "CHOICES")
	private String choices;

	@Column(name = "IS_REQUIRED")
	private boolean isRequired;

	@Column(name = "ID_FORM")
	private Long idForm;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_FORM", insertable = false, updatable = false)
	private Form form;

	@Version
	@Column(name = "VERSION")
	private Long version;

}
