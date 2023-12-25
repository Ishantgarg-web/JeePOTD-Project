package com.example.JEEPOTD.dto;

import com.example.JEEPOTD.enums.DEVCODE;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserAnswerResponse {
	
	/**
	 * This is used to given Response when user hits the api[Request_api_for_validating_the_answer].
	 * API:
	 */
	
	private String problemId;
	
	private String solutionUrl;
	/**
	 * give solutionUrl if any.
	 */
	
	private String correntOption;
	
	private String userChoosedOption;
	
	private String userMessage; 
	/**
	 * userMessage will provide by backend like userchoose option correct or not.
	 * 
	 */
	
	private long timeTook;
	/**
	 * it will display timeTook by user to response for the problem.
	 */
	private DEVCODE devCode;
}
