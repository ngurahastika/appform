package com.example.formapp.repository.appform;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.formapp.model.appform.Responses;

@Repository
public interface ResponsesRepository extends JpaRepository<Responses, Long> {

	@Query(value = "SELECT EXISTS(SELECT 1 FROM responses WHERE id_usr = :idUser AND id_form = :idForm)", nativeQuery = true)
	boolean existsByIdUserAndIdForm(Long idUser, Long idForm);

	@Query(value = "FROM Responses r JOIN FETCH r.user WHERE r.form.idUser = :idUser AND r.form.slug =:slug ")
	List<Responses> getAllResponseFetchUserByIdUserAndSlug(Long idUser, String slug);

}
