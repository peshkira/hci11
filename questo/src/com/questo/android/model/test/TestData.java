package com.questo.android.model.test;

import java.util.Date;

import com.questo.android.ModelManager;
import com.questo.android.helper.Security;
import com.questo.android.helper.UUIDgen;
import com.questo.android.model.Place;
import com.questo.android.model.PossibleAnswer;
import com.questo.android.model.PossibleAnswerMultipleChoice;
import com.questo.android.model.Question;
import com.questo.android.model.Question.Type;
import com.questo.android.model.Tournament;
import com.questo.android.model.TournamentTask;
import com.questo.android.model.Trophy;
import com.questo.android.model.Trophy.Extras;
import com.questo.android.model.User;

public class TestData {

	public static void generateTestData(ModelManager manager) {
		User mainuser = new User(UUIDgen.getUUID(), new Date());
		mainuser.setEmail("questo@questo.me");
		mainuser.setPasswordSalt("lolcat");
		mainuser.setPasswordHash(Security.md5("questololcat")); // pw = questo ;)
		mainuser.setName("landocalrissian");
		manager.create(mainuser, User.class);
		
		Place stephansdom = new Place(UUIDgen.getUUID(), "Stephansdom");
		manager.create(stephansdom, Place.class);
		Place peterskirche = new Place(UUIDgen.getUUID(), "Peterskirche");
		manager.create(peterskirche, Place.class);
		
		Question q_stephansdom_1 = new Question(UUIDgen.getUUID(), Type.MULTIPLE_CHOICE, "How high is the southern tower of the Stephansdom?");
		PossibleAnswer q_stephansdom_1_answer_1 = new PossibleAnswerMultipleChoice(1, "136,4 meters", true);
		PossibleAnswer q_stephansdom_1_answer_2 = new PossibleAnswerMultipleChoice(2, "133,2 meters", false);
		PossibleAnswer q_stephansdom_1_answer_3 = new PossibleAnswerMultipleChoice(3, "145,9 meters", false);
		PossibleAnswer[] q_stephansdom_1_answers = new PossibleAnswer[]{q_stephansdom_1_answer_1, q_stephansdom_1_answer_2, q_stephansdom_1_answer_3};
		q_stephansdom_1.getPossibleAnswers().setAll(q_stephansdom_1_answers);
		q_stephansdom_1.getCorrectAnswer().set(q_stephansdom_1_answer_1);
		manager.create(q_stephansdom_1, Question.class);
		q_stephansdom_1.setPlace(stephansdom);
		manager.update(q_stephansdom_1, Question.class);
		
		
		Tournament crazyTournament = new Tournament();
		crazyTournament.setName("Crazy Tournament");
		TournamentTask crazyParcourTask1 = new TournamentTask(UUIDgen.getUUID(), crazyTournament, stephansdom);
		manager.create(crazyTournament, Tournament.class);
		manager.create(crazyParcourTask1, TournamentTask.class);
		
		Trophy threeHeadedMonkey = new Trophy(UUIDgen.getUUID(),  "Three Headed Monkey", com.questo.android.model.Trophy.Type.GLOBAL);
		manager.create(threeHeadedMonkey, Trophy.class);
		
		
	}
	
}
