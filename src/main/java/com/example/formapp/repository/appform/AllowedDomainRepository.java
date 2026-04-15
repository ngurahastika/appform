package com.example.formapp.repository.appform;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.formapp.model.appform.AllowedDomain;

@Repository
public interface AllowedDomainRepository extends JpaRepository<AllowedDomain, Long> {

	@Query(value = "SELECT ad.domain FROM AllowedDomain ad WHERE ad.idForm = :idForm ")
	List<String> getAllByIdForm(Long idForm);

}
