package com.example.JEEPOTD.dto;

import javax.annotation.Generated;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import com.example.JEEPOTD.enums.DEVCODE;
import com.example.JEEPOTD.enums.Level;
import com.example.JEEPOTD.enums.MCQ;
import com.example.JEEPOTD.enums.Subject;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetProblemRequest {
	
	/**
	 * This Request shows when user hits the api to get today POTD for solve.
	 */
	private DEVCODE devCode;
	
	private String problemUrl; // it shows image url of problem statement with options(if applicable)
	
	@Enumerated(EnumType.STRING)
	private Subject subject;
	
	@Enumerated(EnumType.STRING)
	private MCQ isMcq;
	
	private int previousYear;
	
	private String problemId;
	
	private Level problemLevel;
	
	private String userMessage;
}
