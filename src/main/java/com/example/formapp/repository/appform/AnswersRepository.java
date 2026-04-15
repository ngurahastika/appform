package com.example.formapp.repository.appform;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.formapp.model.appform.Answers;

@Repository
public interface AnswersRepository extends JpaRepository<Answers, Long> {

	@Query(value = "SELECT ans.idResponse, ans.questions.name,  ans.value  FROM  Answers ans  WHERE ans.idResponse IN( :idsResponse ) ORDER BY ans.idResponse")
	public List<Object[]> getQuestionAndAnswersByIdResponse(Set<Long> idsResponse);

}
