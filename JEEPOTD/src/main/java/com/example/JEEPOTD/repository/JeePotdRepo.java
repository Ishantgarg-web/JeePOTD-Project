package com.example.JEEPOTD.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.JEEPOTD.entity.ProblemType;
import com.example.JEEPOTD.enums.Subject;

@Repository
public interface JeePotdRepo extends JpaRepository<ProblemType, Integer>{

	
//	@Query(value = "select * from problem_type where problem_show_on_date=:localDate and subject='PHYSICS'", nativeQuery = true)
//	ProblemType findbytodayDate(LocalDate localDate, Subject subject1);
	
	@Query(value = "select * from problem_type where problem_show_on_date=:localDate and subject='PHYSICS'", nativeQuery = true)
	ProblemType findPhysicsbytodayDate(LocalDate localDate);
	
	@Query(value = "select * from problem_type where problem_show_on_date=:localDate and subject='CHEMISTRY'", nativeQuery = true)
	ProblemType findChemistrybytodayDate(LocalDate localDate);
	
	@Query(value = "select * from problem_type where problem_show_on_date=:localDate and subject='MATH'", nativeQuery = true)
	ProblemType findMathbytodayDate(LocalDate localDate);
	
	@Query(value = "select problem_id from problem_type where is_present = 'YES' and subject='PHYSICS'", nativeQuery = true)
	List<String> findPhysicsAllNewProblem();
	
	@Query(value = "select problem_id from problem_type where is_present = 'YES' and subject='CHEMISTRY'", nativeQuery = true)
	List<String> findChemistryAllNewProblem();
	
	@Query(value = "select problem_id from problem_type where is_present = 'YES' and subject='MATH'", nativeQuery = true)
	List<String> findMathAllNewProblem();
	
	ProblemType findByProblemId(String problemId);
	
	ProblemType findByProblemurl(String problemUrl);
	
	@Query(value = "select * from problem_type where is_present = 'NO' and choosed_option_correct is not null", nativeQuery = true)
	List<ProblemType> getAllPreviousProblems();
	
	@Query(value = "select * from problem_type where is_present = 'NO' and user_choosed_option is null", nativeQuery = true)
	ProblemType findPendingProblem();

	@Query(value = "select problem_id from problem_type where is_present = 'YES'", nativeQuery = true)
	List<String> findAllNewProblem();
	
	@Query(value = "select * from problem_type where is_present = 'NO' and user_choosed_option = 'S'", nativeQuery = true)
	List<ProblemType> findAllSkippedProblems();
	
	@Query(value = "select problem_id from problem_type where is_present = 'YES' and subject='PHYSICS'", nativeQuery = true)
	List<String> findPhysicsProblem();
	
	@Query(value = "select problem_id from problem_type where is_present = 'YES' and subject='CHEMISTRY'", nativeQuery = true)
	List<String> findChemistryProblem();
	
	@Query(value = "select problem_id from problem_type where is_present = 'YES' and subject='MATH'", nativeQuery = true)
	List<String> findMathProblem();
	
	
	
	
	
}
