package com.example.formapp.model.appform;

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
@Table(name = "allowed_domain")
@Entity(name = "AllowedDomain")
public class AllowedDomain {

	@Id
	@Column(name = "ID")
	private Long id;

	@Column(name = "DOM")
	private String domain;

	@Column(name = "ID_FORM")
	private Long idForm;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_FORM", insertable = false, updatable = false)
	private Form form;

	@Version
	@Column(name = "VERSION")
	private Long version;

}
