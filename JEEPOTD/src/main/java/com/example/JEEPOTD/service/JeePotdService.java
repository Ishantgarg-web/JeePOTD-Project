package com.example.JEEPOTD.service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.JEEPOTD.constants.GlobalConstants;
import com.example.JEEPOTD.dto.GetProblemRequest;
import com.example.JEEPOTD.dto.UserAnswerRequest;
import com.example.JEEPOTD.dto.UserAnswerResponse;
import com.example.JEEPOTD.entity.ProblemType;
import com.example.JEEPOTD.enums.ChoosedOptionCorrect;
import com.example.JEEPOTD.enums.DEVCODE;
import com.example.JEEPOTD.enums.Present;
import com.example.JEEPOTD.enums.Subject;
import com.example.JEEPOTD.repository.JeePotdRepo;



@Service
public class JeePotdService {
	
	@Autowired
	JeePotdRepo jeePotdRepo;
	
	private static Logger logger = org.slf4j.LoggerFactory.getLogger(JeePotdService.class);
	
	public GetProblemRequest getPhysicsTodayProblem() {
		 /**
		  * This function is used to return problem request for the day.
		  * Condition:
		  * 1. Check if today date == problemShowOnDate, it means show that problem id only.
		  * 2. check if there is any problem exist that is not shown yet. return List<problemId> al;
		  * 3. if al.size()>0, then show random problem from that and save the date on which this
		  *    problem is shown.
		  * 4. show error message.(in v2 -> we will show problem that for user gave wrong option).
		  */
		
		return getTodayProblem(Subject.PHYSICS);
	}
	
	public GetProblemRequest getChemistryTodayProblem() {
		 /**
		  * This function is used to return problem request for the day.
		  * Condition:
		  * 1. Check if today date == problemShowOnDate, it means show that problem id only.
		  * 2. check if there is any problem exist that is not shown yet. return List<problemId> al;
		  * 3. if al.size()>0, then show random problem from that and save the date on which this
		  *    problem is shown.
		  * 4. show error message.(in v2 -> we will show problem that for user gave wrong option).
		  */
		
		return getTodayProblem(Subject.CHEMISTRY);
	}
	
	public GetProblemRequest getMathTodayProblem() {
		 /**
		  * This function is used to return problem request for the day.
		  * Condition:
		  * 1. Check if today date == problemShowOnDate, it means show that problem id only.
		  * 2. check if there is any problem exist that is not shown yet. return List<problemId> al;
		  * 3. if al.size()>0, then show random problem from that and save the date on which this
		  *    problem is shown.
		  * 4. show error message.(in v2 -> we will show problem that for user gave wrong option).
		  */
		
		return getTodayProblem(Subject.MATH);
	}
	
	private GetProblemRequest getTodayProblem(Subject subject) {
		LocalDate todayDate = LocalDate.now(); // 2023-11-25
		ProblemType pendingProblem = jeePotdRepo.findPendingProblem();
		/**
		 * improved findPendingProblem method on 08-12-23 with release of skipped problem.
		 */
		if(pendingProblem!=null) {
			return new GetProblemRequest().builder()
					.devCode(DEVCODE.SUCCESS)
					.isMcq(pendingProblem.getIsMcq())
					.previousYear(pendingProblem.getPreviousYear())
					.problemId(pendingProblem.getProblemId())
					.problemLevel(pendingProblem.getProblemLevel())
					.problemUrl(pendingProblem.getProblemurl())
					.subject(pendingProblem.getSubject())
					.userMessage("This is a pending problem. Please complete this first")
					.build();	
		}else {
			// It means, there is no pending problem.
			List<String> al = new ArrayList<>();
			if(subject.equals(Subject.PHYSICS)) {
				al = jeePotdRepo.findPhysicsProblem();
			}else if(subject.equals(Subject.CHEMISTRY)) {
				al = jeePotdRepo.findChemistryProblem();
			}else {
				al = jeePotdRepo.findMathProblem();
			}
			if(al.size()==0) {
				// it means there is no fresh problem present.
				return new GetProblemRequest().builder()
						.devCode(DEVCODE.FAILURE)
						.userMessage("There is no problems present")
						.build();
			}
			Random random = new Random();
			int radmonIdx = random.nextInt(al.size());
			String problemIdForShow = al.get(radmonIdx);
			logger.info("problemIdForShow: "+problemIdForShow);
			// Now, we have problemId. Now we need to generate GetProblemRequest from this problemIdforShow.
			ProblemType currentProblemType = jeePotdRepo.findByProblemId(problemIdForShow);
			currentProblemType.setIsPresent(Present.NO);
			currentProblemType.setProblemShowOnDate(todayDate);
			
			// start time for problem.
			if(currentProblemType.getStartTime()==null) {
				currentProblemType.setStartTime(LocalDateTime.now());
			}
			jeePotdRepo.save(currentProblemType);
			
			
			return new GetProblemRequest().builder()
					.devCode(DEVCODE.SUCCESS)
					.isMcq(currentProblemType.getIsMcq())
					.previousYear(currentProblemType.getPreviousYear())
					.problemId(currentProblemType.getProblemId())
					.problemLevel(currentProblemType.getProblemLevel())
					.problemUrl(currentProblemType.getProblemurl())
					.subject(currentProblemType.getSubject())
					.userMessage("problem fetched successfully")
					.build();
		}
	}

	/**
	 * 
	 * @param problemType
	 * Below method is used till 17-12-23(update) it is returning the single problem on that date only.
	 * and its modified to getting continuously problems 1 by 1.
	 * @return
	 */
//	private GetProblemRequest getTodayProblem(Subject subject) {
//		LocalDate todayDate = LocalDate.now(); // 2023-11-25
//		// ProblemType problemType = jeePotdRepo.findbytodayDate(todayDate, subject);
//		ProblemType problemType;
//		if(subject.equals(Subject.PHYSICS)) {
//			problemType = jeePotdRepo.findPhysicsbytodayDate(todayDate);
//		}else if(subject.equals(Subject.CHEMISTRY)) {
//			problemType = jeePotdRepo.findChemistrybytodayDate(todayDate);
//		}else {
//			problemType = jeePotdRepo.findMathbytodayDate(todayDate);
//		}
//		logger.info("problemType: "+problemType);
//		String problemIdForShow;
//		if(problemType == null) {
//			// it means, we have to return new problemStatement.
//			List<String> al;
//			if(subject.equals(Subject.PHYSICS)) {
//				al = jeePotdRepo.findPhysicsAllNewProblem();
//			}else if(subject.equals(Subject.CHEMISTRY)) {
//				al = jeePotdRepo.findChemistryAllNewProblem();
//			}else {
//				al = jeePotdRepo.findMathAllNewProblem();
//			}
//			if(al.size() == 0) {
//				// it means there is no fresh problem present.
//				return new GetProblemRequest().builder()
//						.devCode(DEVCODE.FAILURE)
//						.userMessage("There is no problems present")
//						.build();
//			}
//			Random random = new Random();
//			int radmonIdx = random.nextInt(al.size());
//			problemIdForShow = al.get(radmonIdx);
//		}else {
//			// Now, we have to shown old problem here.
//			problemIdForShow = problemType.getProblemId();
//		}
//		// Now, we have problemId. Now we need to generate GetProblemRequest from this problemIdforShow.
//		ProblemType currentProblemType = jeePotdRepo.findByProblemId(problemIdForShow);
//		currentProblemType.setIsPresent(Present.NO);
//		currentProblemType.setProblemShowOnDate(todayDate);
//		
//		// start time for problem.
//		if(currentProblemType.getStartTime()==null) {
//			currentProblemType.setStartTime(LocalDateTime.now());
//		}
//		jeePotdRepo.save(currentProblemType);
//		
//		
//		return new GetProblemRequest().builder()
//				.devCode(DEVCODE.SUCCESS)
//				.isMcq(currentProblemType.getIsMcq())
//				.previousYear(currentProblemType.getPreviousYear())
//				.problemId(currentProblemType.getProblemId())
//				.problemLevel(currentProblemType.getProblemLevel())
//				.problemUrl(currentProblemType.getProblemurl())
//				.subject(currentProblemType.getSubject())
//				.userMessage("problem fetched successfully")
//				.build();	
//	}

	public String createProblem(ProblemType problemType) {
		problemType.setIsPresent(Present.YES);
		problemType.setCommentByUser("");
		return (jeePotdRepo.save(problemType)!=null)?"Saved successfully":"There is error in saving data";
	}

	public boolean problemUrlPresent(String currentProblemUrl) {
		
		ProblemType problemType = jeePotdRepo.findByProblemurl(currentProblemUrl);
		return (problemType==null)?false:true;
	}
	
	
	public UserAnswerResponse getUserResponseAfterSubmitProblem(UserAnswerRequest userAnswerRequest) {
		/**
		 * This method is used to return AnswerResponse based on userRequest for the today problem.
		 * Condition:
		 * 1. Check the given problemid sent by userAnswerRequest is valid or not.
		 *    valid means: given problemid should be associated to todaydate problem.
		 * 2. if valid:
		 *    	then match answer given by user with savedAnswer. if its matched then give
		 *      positive response to user and modified the saved problem accordingly.
		 *      else:
		 *       return negative response to user. and modified saved problem accordingly.
		 * 3. user can hit the api to submit answer atmost 1 time.
		 */
		if(validateUserAnswerRequest(userAnswerRequest)) {
			ProblemType currentProblemType = jeePotdRepo.findByProblemId(userAnswerRequest.getProblemId());
			if(currentProblemType.getStartTime() == null) {
				return new UserAnswerResponse().builder()
						.devCode(DEVCODE.SUCCESS)
						.problemId(userAnswerRequest.getProblemId())
						.userMessage("Please fetch your today problem again!!")
						.build();
			}
			if(userHitAPIAgain(userAnswerRequest)) {
				return new UserAnswerResponse().builder()
						.devCode(DEVCODE.FAILURE)
						.userMessage("You can not submit same problem again")
						.problemId(userAnswerRequest.getProblemId())
						.correntOption(currentProblemType.getCorrectOption())
						.userChoosedOption(currentProblemType.getUserChoosedOption())
						.solutionUrl(currentProblemType.getSolutionurl())
						.build();
			}
			/**
			 * Here, logic is added for skipping the problem.
			 */
			if(userAnswerRequest.getOption().equalsIgnoreCase(GlobalConstants.SKIP_QUESTION_SYMBOL)) {
				// it means, user want to skip this problem.
				currentProblemType.setUserChoosedOption(userAnswerRequest.getOption().toUpperCase());
				if(currentProblemType.getEndTime()!=null) {
					// it means, user want to skip this problem again.
					long timeTookTillNow = currentProblemType.getTimeTookToSolveProblem();
					timeTookTillNow+=Duration.between(currentProblemType.getStartTime(), LocalDateTime.now()).getSeconds();
					currentProblemType.setTimeTookToSolveProblem(timeTookTillNow);
					currentProblemType.setEndTime(LocalDateTime.now());
					currentProblemType.setCommentByUser(getSkippedCommentForUser(currentProblemType.getCommentByUser(),userAnswerRequest.getComment()));
					jeePotdRepo.save(currentProblemType);
					return new UserAnswerResponse().builder()
							.devCode(DEVCODE.SUCCESS)
							.problemId(userAnswerRequest.getProblemId())
							.userChoosedOption(userAnswerRequest.getOption())
							.timeTook(currentProblemType.getTimeTookToSolveProblem())
							.userMessage("Your problem is skipped again.You can attempt other problem")
							.build();
				}
				currentProblemType.setEndTime(LocalDateTime.now());
				Duration duration = Duration.between(currentProblemType.getStartTime(), currentProblemType.getEndTime());
				currentProblemType.setCommentByUser(userAnswerRequest.getComment()+"#"+GlobalConstants.SKIP_QUESTION_TEXT);
				currentProblemType.setTimeTookToSolveProblem(duration.getSeconds());
				jeePotdRepo.save(currentProblemType);
				return new UserAnswerResponse().builder()
						.devCode(DEVCODE.SUCCESS)
						.problemId(userAnswerRequest.getProblemId())
						.userChoosedOption(userAnswerRequest.getOption())
						.timeTook(duration.getSeconds())
						.userMessage("Your problem is skipped for now!!.You can attempt other problem")
						.build();
			}
			// Now, match answer key saved in database.
			// send endTime as of now.
			currentProblemType.setEndTime(LocalDateTime.now());
			currentProblemType.setUserChoosedOption(userAnswerRequest.getOption().toUpperCase());
			// logger.info("endTime: "+GlobalTimeTookConstants.endTime+" startTime: "+GlobalTimeTookConstants.startTime);
			Duration duration = Duration.between(currentProblemType.getStartTime(), currentProblemType.getEndTime());
			/**
			 * Here we will do extra for skipped problem.
			 */
			if(currentProblemType.getCommentByUser()!=null && currentProblemType.getCommentByUser().contains(GlobalConstants.SKIP_QUESTION_TEXT)) {
				// it means, this request is come for validating the skipped problem.
				currentProblemType.setCommentByUser(getSkippedCommentForUser(currentProblemType.getCommentByUser(), userAnswerRequest.getComment()));
				currentProblemType.setTimeTookToSolveProblem(
							currentProblemType.getTimeTookToSolveProblem()+
							duration.getSeconds()
						);
			}else {
				currentProblemType.setCommentByUser(userAnswerRequest.getComment());
				currentProblemType.setTimeTookToSolveProblem(duration.getSeconds());
			}
			if(userAnswerRequest.getOption().equalsIgnoreCase(currentProblemType.getCorrectOption())) {
				// it means user give correct answer.
				currentProblemType.setChoosedOptionCorrect(ChoosedOptionCorrect.CORRECT);
				jeePotdRepo.save(currentProblemType);
				return new UserAnswerResponse().builder()
						.devCode(DEVCODE.SUCCESS)
						.problemId(userAnswerRequest.getProblemId())
						.solutionUrl(currentProblemType.getSolutionurl())
						.correntOption(currentProblemType.getCorrectOption())
						.userChoosedOption(userAnswerRequest.getOption())
						.timeTook(currentProblemType.getTimeTookToSolveProblem())
						.userMessage("Congratulations!! This is a correct Answer")
						.build();
			}else {
				// it means user give wrong answer.
				currentProblemType.setChoosedOptionCorrect(ChoosedOptionCorrect.WRONG);
				jeePotdRepo.save(currentProblemType);
				return new UserAnswerResponse().builder()
						.devCode(DEVCODE.SUCCESS)
						.problemId(userAnswerRequest.getProblemId())
						.solutionUrl(currentProblemType.getSolutionurl())
						.correntOption(currentProblemType.getCorrectOption())
						.userChoosedOption(userAnswerRequest.getOption())
						.timeTook(currentProblemType.getTimeTookToSolveProblem())
						.userMessage("You gave wrong answer")
						.build();
				
			}
		}else {
			// it means, it is not a valid problemId
			return new UserAnswerResponse().builder()
					.devCode(DEVCODE.FAILURE)
					.problemId(userAnswerRequest.getProblemId())
					.solutionUrl(null)
					.userMessage("It is not a valid problemId or you try to submit problem again")
					.timeTook(0)
					.build();
		}
	}
	
	private String getSkippedCommentForUser(String previousComment, String newComment) {
		String s[] = previousComment.split("#");
		StringBuilder sb = new StringBuilder();
		sb.append(s[0]);
		for (int i=1;i<s.length-1;i++) {
			sb.append("#"+s[i]);
		}
		sb.append("#"+newComment);
		sb.append("#"+GlobalConstants.SKIP_QUESTION_TEXT);
		return sb.toString();
	}
	
	private boolean userHitAPIAgain(UserAnswerRequest userAnswerRequest) {
		ProblemType currenProblemType = jeePotdRepo.findByProblemId(userAnswerRequest.getProblemId());
		if(currenProblemType.getChoosedOptionCorrect()!=null) {
			return true;
		}
		return false;
	}

	private boolean validateUserAnswerRequest(UserAnswerRequest userAnswerRequest) {
		// Check if given problemId present in db and also mapped to todayDate with problem_show_on_date field.
		ProblemType currenProblemType = jeePotdRepo.findByProblemId(userAnswerRequest.getProblemId());
		if(currenProblemType!=null) {
			LocalDate todayDate = LocalDate.now();
			if(currenProblemType.getProblemShowOnDate().equals(todayDate)) {
				return true;
			}
		}
		return false;
	}

	public List<ProblemType> getAllPreviousProblem() {
		List<ProblemType> allPreviousProblems = jeePotdRepo.getAllPreviousProblems();
		return allPreviousProblems;
	}

	public GetProblemRequest getProblem() {
		/**
		 * This method is used for returing the problem continuosuly 1 by 1 of any random subject.
		 * User will solve given problem, and when user submit the answer of that problem, user
		 * will get another problem of any subject.
		 * This does not intersect with daily POTD. That was doing their work seperately.
		 * 
		 * How does this work?
		 * 1. we will check, if there is any pending problem(problems for which user not submitted answer, should not contain skipped problems)
		 *    there for which user not submit the answer.
		 * 2. if there, then we will display that same problem only.
		 * 3. if not, then we will display new problem. here that not applied any check correspond
		 *	  to check if todayDate problem already show or not.
		 *
		 */
		LocalDate todayDate = LocalDate.now(); // 2023-11-25
		ProblemType pendingProblem = jeePotdRepo.findPendingProblem();
		/**
		 * improved findPendingProblem method on 08-12-23 with release of skipped problem.
		 */
		if(pendingProblem!=null) {
			return new GetProblemRequest().builder()
					.devCode(DEVCODE.SUCCESS)
					.isMcq(pendingProblem.getIsMcq())
					.previousYear(pendingProblem.getPreviousYear())
					.problemId(pendingProblem.getProblemId())
					.problemLevel(pendingProblem.getProblemLevel())
					.problemUrl(pendingProblem.getProblemurl())
					.subject(pendingProblem.getSubject())
					.userMessage("This is a pending problem. Please complete this first")
					.build();	
		}else {
			// It means, there is no pending problem.
			List<String> al = jeePotdRepo.findAllNewProblem();
			if(al.size()==0) {
				// it means there is no fresh problem present.
				return new GetProblemRequest().builder()
						.devCode(DEVCODE.FAILURE)
						.userMessage("There is no problems present")
						.build();
			}
			Random random = new Random();
			int radmonIdx = random.nextInt(al.size());
			String problemIdForShow = al.get(radmonIdx);
			// Now, we have problemId. Now we need to generate GetProblemRequest from this problemIdforShow.
			ProblemType currentProblemType = jeePotdRepo.findByProblemId(problemIdForShow);
			currentProblemType.setIsPresent(Present.NO);
			currentProblemType.setProblemShowOnDate(todayDate);
			
			// start time for problem.
			if(currentProblemType.getStartTime()==null) {
				currentProblemType.setStartTime(LocalDateTime.now());
			}
			jeePotdRepo.save(currentProblemType);
			
			
			return new GetProblemRequest().builder()
					.devCode(DEVCODE.SUCCESS)
					.isMcq(currentProblemType.getIsMcq())
					.previousYear(currentProblemType.getPreviousYear())
					.problemId(currentProblemType.getProblemId())
					.problemLevel(currentProblemType.getProblemLevel())
					.problemUrl(currentProblemType.getProblemurl())
					.subject(currentProblemType.getSubject())
					.userMessage("problem fetched successfully")
					.build();	
		}
	}

	public List<GetProblemRequest> getSkippedProblem() {
		/**
		 * Here, we will return all problems skipped by user.
		 * How we identify userSkipped particular problem or not.
		 * if problemType.user_choosed_option == "S", it means, user skipped this problem.
		 */
		List<ProblemType> allSkippedProblems = jeePotdRepo.findAllSkippedProblems();
		List<GetProblemRequest> res = new ArrayList<>();
		if(allSkippedProblems.size()==0) {
			GetProblemRequest getProblemRequest =  new GetProblemRequest().builder()
					.devCode(DEVCODE.FAILURE)
					.userMessage("There is no problems Skipped by you")
					.build();
			res.add(getProblemRequest);
			return res;
		}else {
			for (ProblemType problemType: allSkippedProblems) {
				problemType.setStartTime(LocalDateTime.now());
				jeePotdRepo.save(problemType);
				GetProblemRequest getProblemRequest =  new GetProblemRequest().builder()
						.devCode(DEVCODE.SUCCESS)
						.isMcq(problemType.getIsMcq())
						.previousYear(problemType.getPreviousYear())
						.problemId(problemType.getProblemId())
						.problemLevel(problemType.getProblemLevel())
						.problemUrl(problemType.getProblemurl())
						.subject(problemType.getSubject())
						.userMessage("problem fetched successfully")
						.build();	
				res.add(getProblemRequest);
			}
			return res;
		}
	}
	
	
}
