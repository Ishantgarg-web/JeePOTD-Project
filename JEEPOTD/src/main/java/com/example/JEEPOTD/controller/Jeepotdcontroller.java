package com.example.JEEPOTD.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.JEEPOTD.dto.GetProblemRequest;
import com.example.JEEPOTD.dto.UserAnswerRequest;
import com.example.JEEPOTD.dto.UserAnswerResponse;
import com.example.JEEPOTD.entity.ProblemType;
import com.example.JEEPOTD.enums.DEVCODE;
import com.example.JEEPOTD.repository.JeePotdRepo;
import com.example.JEEPOTD.service.JeePotdService;

@RestController
public class Jeepotdcontroller {
	
	@Autowired
	JeePotdService jeePotdService;
	
	/**
	 * Below API is use to getTodayProblem.
	 * So, i will create 3 different apis to fetch the each subject problem seperately.
	 * @return
	 */
	
	@GetMapping("/home")
	private String homePage() {
		return "It is working fine!!";
	}
	
	@GetMapping("/getTodayProblem/P")
	private ResponseEntity<GetProblemRequest> todayPhysicsPotd() {
		GetProblemRequest getProblemRequest = jeePotdService.getPhysicsTodayProblem();
		if(getProblemRequest.getDevCode().equals(DEVCODE.SUCCESS)) {
			return new ResponseEntity<>(getProblemRequest, HttpStatus.OK);
		}else {
			return new ResponseEntity<GetProblemRequest>(getProblemRequest, HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping("/getTodayProblem/C")
	private ResponseEntity<GetProblemRequest> todayChemistryPotd() {
		GetProblemRequest getProblemRequest = jeePotdService.getChemistryTodayProblem();
		if(getProblemRequest.getDevCode().equals(DEVCODE.SUCCESS)) {
			return new ResponseEntity<>(getProblemRequest, HttpStatus.OK);
		}else {
			return new ResponseEntity<GetProblemRequest>(getProblemRequest, HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping("/getTodayProblem/M")
	private ResponseEntity<GetProblemRequest> todayMathPotd() {
		GetProblemRequest getProblemRequest = jeePotdService.getMathTodayProblem();
		if(getProblemRequest.getDevCode().equals(DEVCODE.SUCCESS)) {
			return new ResponseEntity<>(getProblemRequest, HttpStatus.OK);
		}else {
			return new ResponseEntity<GetProblemRequest>(getProblemRequest, HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping("/getProblem")
	private ResponseEntity<GetProblemRequest> getProblem(){
		GetProblemRequest getProblemRequest = jeePotdService.getProblem();
		if(getProblemRequest.getDevCode().equals(DEVCODE.SUCCESS)) {
			return new ResponseEntity<>(getProblemRequest, HttpStatus.OK);
		}else {
			return new ResponseEntity<GetProblemRequest>(getProblemRequest, HttpStatus.BAD_REQUEST);
		}
	}
	
	/**
	 * Below API used to getAllSkipeed problems.
	 * @return
	 */
	@GetMapping("/getSkippedProblems")
	private ResponseEntity<List<GetProblemRequest>> getSkippedProblem(){
		List<GetProblemRequest> getProblemRequest = jeePotdService.getSkippedProblem();
		return new ResponseEntity<List<GetProblemRequest>>(getProblemRequest, HttpStatus.ACCEPTED);
	}
	
	/**
	 * 
	 * @param problemType
	 * Below API is used to create Problem in database.
	 * @return
	 */
	@PostMapping("/saveProblem")
	private String createProblem(@RequestBody List<ProblemType> problemTypeList){
		List<String> problemIdList = null; // will store the problemIds.
		for (ProblemType problemType: problemTypeList) {
			String currentProblemUrl = problemType.getProblemurl();
			if(jeePotdService.problemUrlPresent(currentProblemUrl)) {
				problemIdList.add(problemType.getProblemId());
			}else {
				jeePotdService.createProblem(problemType);
			}
		}
		if(problemIdList == null || problemIdList.isEmpty()) {
			return "All problems are saved successfully";
		}else {
			String res = problemIdList.get(0);
			for (int i=1;i<problemIdList.size();i++) {
				res = res + "#" + problemIdList.get(i);
			}
			return res;
		}
	}
	
	/***
	 * Below API is used to submit userAnswer and in return it will give the response of the user.
	 */
	@PostMapping("/userSubmitAnswer")
	private ResponseEntity<UserAnswerResponse> getUserResponse(@RequestBody UserAnswerRequest userAnswerRequest){
		UserAnswerResponse userAnswerResponse = jeePotdService.getUserResponseAfterSubmitProblem(userAnswerRequest);
		if(userAnswerResponse.getDevCode()==DEVCODE.SUCCESS) {
			return new ResponseEntity<>(userAnswerResponse, HttpStatus.OK);
		}else {
			return new ResponseEntity<>(userAnswerResponse, HttpStatus.BAD_REQUEST);
		}
	}
	
	/***
	 * Below API is used to getAll solved problems previously by the user.
	 */
	@GetMapping("/getAllPreviousProblems")
	private ResponseEntity<List<ProblemType>> getAllSolvedProblems(){
		List<ProblemType> allPreviousProblems = jeePotdService.getAllPreviousProblem();
		if(allPreviousProblems == null) {
			return null;
		}else {
			return new ResponseEntity<>(allPreviousProblems, HttpStatus.OK);
		}
	}
	
}
