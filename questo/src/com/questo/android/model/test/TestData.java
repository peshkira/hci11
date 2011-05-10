package com.questo.android.model.test;

import java.util.Date;
import java.util.UUID;

import com.questo.android.ModelManager;
import com.questo.android.helper.Security;
import com.questo.android.helper.UUIDgen;
import com.questo.android.model.Place;
import com.questo.android.model.PossibleAnswer;
import com.questo.android.model.PossibleAnswerImpl;
import com.questo.android.model.PossibleAnswerMultipleChoice;
import com.questo.android.model.PossibleAnswerNumberGuessing;
import com.questo.android.model.Question;
import com.questo.android.model.TournamentMembership;
import com.questo.android.model.TournamentRequest;
import com.questo.android.model.TournamentTaskDone;
import com.questo.android.model.Question.Type;
import com.questo.android.model.Tournament;
import com.questo.android.model.TournamentTask;
import com.questo.android.model.Trophy;
import com.questo.android.model.User;

public class TestData {
	
	public static User generateTestData(ModelManager manager) {
		User mainuser = new User(UUIDgen.getUUID(), new Date());
		mainuser.setEmail("questo@questo.me");
		mainuser.setPasswordSalt("lolcat");
		mainuser.setPasswordHash(Security.md5("questololcat")); // pw = questo ;)
		mainuser.setName("landocalrissian");
		manager.create(mainuser, User.class);
		
		User xyUser = new User(UUIDgen.getUUID(), new Date());
		xyUser.setEmail("xy@xy.com");
		xyUser.setPasswordSalt("lolcat");
		xyUser.setPasswordHash(Security.md5("xy")); // pw = questo ;)
		xyUser.setName("Xavier Ypsilon");
		manager.create(xyUser, User.class);
		
		Place stephansdom = new Place(UUIDgen.getUUID(), "Stephansdom");
		manager.create(stephansdom, Place.class);
		Place peterskirche = new Place(UUIDgen.getUUID(), "Peterskirche");
		manager.create(peterskirche, Place.class);
		
		Question q_stephansdom_1 = new Question(UUIDgen.getUUID(), Type.MULTIPLE_CHOICE, "How high is the southern tower of the Stephansdom?");
		PossibleAnswerImpl q_stephansdom_1_answer_1 = new PossibleAnswerMultipleChoice(1, "136,4 meters", true);
		PossibleAnswerImpl q_stephansdom_1_answer_2 = new PossibleAnswerMultipleChoice(2, "133,2 meters", false);
		PossibleAnswerImpl q_stephansdom_1_answer_3 = new PossibleAnswerMultipleChoice(3, "145,9 meters", false);
		PossibleAnswer[] q_stephansdom_1_answers = new PossibleAnswer[]{q_stephansdom_1_answer_1, q_stephansdom_1_answer_2, q_stephansdom_1_answer_3};
		q_stephansdom_1.getPossibleAnswers().setAll(q_stephansdom_1_answers);
		q_stephansdom_1.getCorrectAnswer().set(q_stephansdom_1_answer_1);
		manager.create(q_stephansdom_1, Question.class);
		q_stephansdom_1.setPlace(stephansdom);
		manager.update(q_stephansdom_1, Question.class);
		
		Question q_stephansdom_2 = new Question(UUIDgen.getUUID(), Type.NUMBERS_GUESSING, "During which year was the St. Stephen Cathedral in Vienna built?");
        PossibleAnswer[] q_stephansdom_2_answers = new PossibleAnswer[]{};
        PossibleAnswerImpl q_stephansdom_2_answer = new PossibleAnswerNumberGuessing("1147");
        q_stephansdom_2.getPossibleAnswers().setAll(q_stephansdom_2_answers);
        q_stephansdom_2.getCorrectAnswer().set(q_stephansdom_2_answer);
        manager.create(q_stephansdom_2, Question.class);
        q_stephansdom_2.setPlace(stephansdom);
        manager.update(q_stephansdom_2, Question.class);
        
		Tournament crazyTournament = new Tournament(UUIDgen.getUUID(), new Date(), "Crazy Tournament", "Vienna", Tournament.Type.COOP);
		TournamentTask crazyParcourTask1 = new TournamentTask(UUIDgen.getUUID(), crazyTournament, stephansdom);
		TournamentTask crazyParcourTask2 = new TournamentTask(UUIDgen.getUUID(), crazyTournament, stephansdom);
		TournamentTaskDone crazyParcourTask1Done = new TournamentTaskDone(UUIDgen.getUUID(), mainuser, crazyParcourTask1.getUuid(), new Date());
		TournamentMembership crazyTournamentMembership = new TournamentMembership(UUIDgen.getUUID(), mainuser, crazyTournament, new Date());
		manager.create(crazyTournament, Tournament.class);
		manager.create(crazyParcourTask1, TournamentTask.class);
		manager.create(crazyParcourTask2, TournamentTask.class);
		manager.create(crazyTournamentMembership, TournamentMembership.class);
		manager.create(crazyParcourTask1Done, TournamentTaskDone.class);
		
		Tournament deathmatchTournament = new Tournament(UUIDgen.getUUID(), new Date(), "Deathmatch", "Vienna", Tournament.Type.COOP);
		TournamentTask deathmatchTask1 = new TournamentTask(UUIDgen.getUUID(), deathmatchTournament, stephansdom);
		TournamentMembership deathmatchTournamentMembershipXY = new TournamentMembership(UUIDgen.getUUID(), xyUser, deathmatchTournament, new Date());
		TournamentRequest deathmatchTournamentRequest = new TournamentRequest(UUIDgen.getUUID(), mainuser, xyUser, deathmatchTournament, new Date());
		manager.create(deathmatchTournament, Tournament.class);
		manager.create(deathmatchTask1, TournamentTask.class);
		manager.create(deathmatchTournamentMembershipXY, TournamentMembership.class);
		manager.create(deathmatchTournamentRequest, TournamentRequest.class);
		
		Trophy threeHeadedMonkey = new Trophy(UUIDgen.getUUID(), "Three Headed Monkey", com.questo.android.model.Trophy.Type.GLOBAL);
		manager.create(threeHeadedMonkey, Trophy.class);
		
		return mainuser;
	}
	
}
