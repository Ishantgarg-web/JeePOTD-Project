package com.example.JEEPOTD.entity;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import com.example.JEEPOTD.enums.Subject;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProblemResponse {
	
	private String problemId;
	
	private String userMessage;
	
	private boolean isMcq;
	
	@Enumerated(EnumType.STRING)
	private Subject subject;
	
	private int timeTook;
	
//	private Correct isCorrect;
	
	private String solutionUrl;
	
}
