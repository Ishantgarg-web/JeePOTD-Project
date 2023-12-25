package com.example.JEEPOTD;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 
 * @author ishant
 * Till now 3 apis are working fine:
 * getTodayProblem, saveProblem, userSubmitAnswer.
 * 
 * Now, need to make API such that:
 * 1. Now, user is getting problem for each subject today. - DONE
 * 2. user get submitted answer. - DONE
 * 3. user get All previous problems. - DONE
 * 4. need to deploy the springBootApp. - DONE
 * 5. user will get mail if user not solved problem by 9pm.
 * 
 * update on 05-12-23:
 * 1. Add API /getProblem to get Problems continuously. not related with POTD.
 *    same Logic also aplies here, problems not repeated.
 * Request Raise to update: 07-12-23:
 * 1. Modify the code such as user have the option to skip the problem also. 
 * 	  Logic is We will show the problem as we are doing, but user now have the option to enter "S"
 *    as in /userSubmitAnswer API. it means, user want to skip this problem. This problem will never
 *    show to the user.
 *    In response of the /userSubmitAnswer API, we will update "userMessage#SKIP" in database.
 * 2. Create another API /getSkipProblems to get all skip problems that user previously skipped.
 *    and when Again user Submitted the answer with message then our modified UserMessage will be:
 *    UserMessage1#UserMessage2#SKIP
 * 3. Total time took for this will be: time took to skip the problem + time took again to attempt the problem.
 * 4. User can skip a particular problem multiple time, but make sure the user first fetched the skipped
 *    problem and then try to submit answer else startTime will never change and it could give wrong
 *    response for timeTookToSolveProblem.
 *    
 *    Done with Above changes on 08-12-23.
 * 
 * update on 17-12-23:
 * 1. update /getTodayProblem API request to get problems subject-wise continuously.
 * 
 * How to Problem_id define?
 * for problem_id:
 * {first_character_of_subject}+{last2digitsofYear}+{shift}+{problem_number}+{if Solution S}
 */


@SpringBootApplication
public class JeepotdApplication {

	public static void main(String[] args) {
		SpringApplication.run(JeepotdApplication.class, args);
	}
}
