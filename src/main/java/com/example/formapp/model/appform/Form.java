package com.example.formapp.model.appform;

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
@Table(name = "form")
@Entity(name = "Form")
public class Form extends CreatorMod {

	@Id
	@Column(name = "ID")
	private Long id;

	@Column(name = "NM")
	private String name;

	@Column(name = "SLUG")
	private String slug;

	@Column(name = "DESCRIPTION")
	private String description;

	@Column(name = "LIMIT_ONE_RESPONSE")
	private boolean limitOneResponse;

	@Column(name = "ID_USR")
	private Long idUser;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_USR", insertable = false, updatable = false)
	private User user;

	@Version
	@Column(name = "VERSION")
	private Long version;

}
