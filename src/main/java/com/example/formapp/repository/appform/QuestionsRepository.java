package com.example.formapp.repository.appform;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.formapp.model.appform.Questions;

@Repository
public interface QuestionsRepository extends JpaRepository<Questions, Long> {

	@Modifying
	@Query(value = "DELETE FROM Questions q WHERE q.id = :idQuestion AND  q.idForm = (SELECT f.id "
			+ " FROM Form f WHERE f.slug = :slug AND f.idUser = :idUser) ", nativeQuery = false)
	public int deleteQuestion(Long idQuestion, String slug, Long idUser);

	@Query(value = "FROM Questions q WHERE q.idForm = :idForm")
	List<Questions> getAllByIdForm(Long idForm);
	
	@Query(value = "SELECT q.id FROM Questions q WHERE q.idForm = :idForm")
	List<Long> getAllIdByIdForm(Long idForm);

}
