package com.example.formapp.repository.appform;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.formapp.model.appform.Form;

@Repository
public interface FormRepository extends JpaRepository<Form, Long> {

	@Query(value = "SELECT f.id FROM Form f WHERE f.slug = :slug")
	public Long checkSlug(String slug);

	@Query(value = "SELECT f.id FROM Form f WHERE f.slug = :slug AND f.idUser = :idUser")
	public Long findIdForm(String slug, Long idUser);

	@Query(value = " FROM Form f WHERE f.slug = :slug ")
	public Optional<Form> findBySlug(String slug);

	@Query(value = " FROM Form f WHERE f.idUser = :idUser")
	public List<Form> listingFormByIdUser(Long idUser);

}
