package com.example.JEEPOTD.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserAnswerRequest {
	
	/**
	 * This Request is sent by user when user solved the given problem and hits the API[] to validate it.
	 */
	private String problemId;
	
	private String option; // option choosed by user or integer value.
	/**
	 * use can also enter "S" as option to skip the problem.
	 */
	
	private String comment; // any comment that user want to give(optional)
}
