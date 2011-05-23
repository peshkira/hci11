package com.questo.android.model.test;

import java.util.Date;

import com.questo.android.ModelManager;
import com.questo.android.helper.Security;
import com.questo.android.helper.UUIDgen;
import com.questo.android.model.Companionship;
import com.questo.android.model.Place;
import com.questo.android.model.PossibleAnswer;
import com.questo.android.model.PossibleAnswerImpl;
import com.questo.android.model.PossibleAnswerMultipleChoice;
import com.questo.android.model.PossibleAnswerNumberGuessing;
import com.questo.android.model.Question;
import com.questo.android.model.Question.Type;
import com.questo.android.model.Tournament;
import com.questo.android.model.TournamentMembership;
import com.questo.android.model.TournamentRequest;
import com.questo.android.model.TournamentTask;
import com.questo.android.model.TournamentTaskDone;
import com.questo.android.model.User;

public class TestDataGenerator {

    private ModelManager manager;

    public TestDataGenerator(ModelManager manager) {
        this.manager = manager;
    }
    
    public void generateTestData() {
        this.generateUsers();
    }

    private void generateUsers() {
        User vader = new User(UUIDgen.getUUID(), new Date());
        vader.setEmail("vader@questo.com");
        vader.setPasswordSalt("lolcat");
        vader.setPasswordHash(Security.md5("questololcat")); // pw = questo
        vader.setName("Darth Vader");
        this.manager.create(vader, User.class);

        User kirk = new User(UUIDgen.getUUID(), new Date());
        kirk.setEmail("kirk@questo.com");
        kirk.setPasswordSalt("lolcat");
        kirk.setPasswordHash(Security.md5("questololcat"));
        kirk.setName("Captain Kirk");
        manager.create(kirk, User.class);

        User gandalf = new User(UUIDgen.getUUID(), new Date());
        gandalf.setEmail("gandalf@questo.com");
        gandalf.setPasswordSalt("lolcat");
        gandalf.setPasswordHash(Security.md5("questololcat"));
        gandalf.setName("Gandalf");
        manager.create(gandalf, User.class);

        User guinevier = new User(UUIDgen.getUUID(), new Date());
        guinevier.setEmail("guinevier@questo.com");
        guinevier.setPasswordSalt("lolcat");
        guinevier.setPasswordHash(Security.md5("questololcat"));
        guinevier.setName("Guinevier");
        manager.create(guinevier, User.class);

        User bender = new User(UUIDgen.getUUID(), new Date());
        bender.setEmail("bender@questo.com");
        bender.setPasswordSalt("lolcat");
        bender.setPasswordHash(Security.md5("questololcat"));
        bender.setName("Bender Rodrigez");
        manager.create(bender, User.class);

        Companionship gand_vader = new Companionship(UUIDgen.getUUID(), gandalf.getUuid(), vader.getUuid(), new Date());
        gand_vader.setConfirmedAt(new Date());
        gand_vader.setConfirmed(true);
        manager.create(gand_vader, Companionship.class);

        Companionship vader_kirk = new Companionship(UUIDgen.getUUID(), vader.getUuid(), kirk.getUuid(), new Date());
        vader_kirk.setConfirmedAt(new Date());
        vader_kirk.setConfirmed(true);
        manager.create(vader_kirk, Companionship.class);
        
        this.generatePlaces(vader, gandalf);

    }

    private void generatePlaces(User mainuser, User second) {
        Place stephansdom = new Place(UUIDgen.getUUID(), "Stephansdom");
        stephansdom.setLatitude(48.208359);
        stephansdom.setLongitude(16.373298);
        manager.create(stephansdom, Place.class);
        Place peterskirche = new Place(UUIDgen.getUUID(), "Peterskirche");
        peterskirche.setLatitude(48.209405);
        peterskirche.setLongitude(16.370088);
        manager.create(peterskirche, Place.class);

        Question q_stephansdom_1 = new Question(UUIDgen.getUUID(), Type.MULTIPLE_CHOICE,
                "How high is the southern tower of the Stephansdom?");
        PossibleAnswerImpl q_stephansdom_1_answer_1 = new PossibleAnswerMultipleChoice(1, "136,4 meters", true);
        PossibleAnswerImpl q_stephansdom_1_answer_2 = new PossibleAnswerMultipleChoice(2, "133,2 meters", false);
        PossibleAnswerImpl q_stephansdom_1_answer_3 = new PossibleAnswerMultipleChoice(3, "145,9 meters", false);
        PossibleAnswer[] q_stephansdom_1_answers = new PossibleAnswer[] { q_stephansdom_1_answer_1,
                q_stephansdom_1_answer_2, q_stephansdom_1_answer_3 };
        q_stephansdom_1.getPossibleAnswers().setAll(q_stephansdom_1_answers);
        q_stephansdom_1.getCorrectAnswer().set(q_stephansdom_1_answer_1);
        manager.create(q_stephansdom_1, Question.class);
        q_stephansdom_1.setPlace(stephansdom);
        manager.update(q_stephansdom_1, Question.class);

        Question q_stephansdom_2 = new Question(UUIDgen.getUUID(), Type.NUMBERS_GUESSING,
                "During which year was the St. Stephen Cathedral in Vienna built?");
        PossibleAnswer[] q_stephansdom_2_answers = new PossibleAnswer[] {};
        PossibleAnswerImpl q_stephansdom_2_answer = new PossibleAnswerNumberGuessing("1147");
        q_stephansdom_2.getPossibleAnswers().setAll(q_stephansdom_2_answers);
        q_stephansdom_2.getCorrectAnswer().set(q_stephansdom_2_answer);
        manager.create(q_stephansdom_2, Question.class);
        q_stephansdom_2.setPlace(stephansdom);
        manager.update(q_stephansdom_2, Question.class);
        
        this.generateTournaments(mainuser, second, stephansdom);

    }
    
    private void generateTournaments(User mainuser, User second, Place stephansdom) {
        Tournament crazyTournament = new Tournament(UUIDgen.getUUID(), new Date(), "Crazy Tournament", "Vienna",
                Tournament.Type.COOP);
        TournamentTask crazyParcourTask1 = new TournamentTask(UUIDgen.getUUID(), crazyTournament, stephansdom.getUuid());
        TournamentTask crazyParcourTask2 = new TournamentTask(UUIDgen.getUUID(), crazyTournament, stephansdom.getUuid());
        TournamentTaskDone crazyParcourTask1Done = new TournamentTaskDone(UUIDgen.getUUID(), mainuser,
                crazyParcourTask1.getUuid(), new Date());
        TournamentMembership crazyTournamentMembership = new TournamentMembership(UUIDgen.getUUID(),
                mainuser.getUuid(), crazyTournament.getUuid(), new Date());
        manager.create(crazyTournament, Tournament.class);
        manager.create(crazyParcourTask1, TournamentTask.class);
        manager.create(crazyParcourTask2, TournamentTask.class);
        manager.create(crazyTournamentMembership, TournamentMembership.class);
        manager.create(crazyParcourTask1Done, TournamentTaskDone.class);

        Tournament deathmatchTournament = new Tournament(UUIDgen.getUUID(), new Date(), "Deathmatch", "Vienna",
                Tournament.Type.COOP);
        TournamentTask deathmatchTask1 = new TournamentTask(UUIDgen.getUUID(), deathmatchTournament,
                stephansdom.getUuid());
        TournamentMembership deathmatchTournamentMembershipXY = new TournamentMembership(UUIDgen.getUUID(),
                second.getUuid(), deathmatchTournament.getUuid(), new Date());
        TournamentRequest deathmatchTournamentRequest = new TournamentRequest(UUIDgen.getUUID(), mainuser.getUuid(),
                second.getUuid(), deathmatchTournament.getUuid(), new Date());
        manager.create(deathmatchTournament, Tournament.class);
        manager.create(deathmatchTask1, TournamentTask.class);
        manager.create(deathmatchTournamentMembershipXY, TournamentMembership.class);
        manager.create(deathmatchTournamentRequest, TournamentRequest.class);

    }

//    public void generateTestData(ModelManager manager) {
//        Trophy threeHeadedMonkey = new Trophy(UUIDgen.getUUID(), "Three Headed Monkey", "Look a three-headed monkey!",
//                com.questo.android.model.Trophy.Type.GLOBAL);
//        manager.create(threeHeadedMonkey, Trophy.class);
//
//        Trophy theonering = new Trophy(UUIDgen.getUUID(), "The One Ring",
//                "One Ring to rule them all,<br /> One Ring to find them,<br /> "
//                        + "One Ring to bring them all<br /> and in the darkness bind them.<br />"
//                        + "In the Land of Mordor where the Shadows lie.",
//                com.questo.android.model.Trophy.Type.FOR_PLACE);
//        manager.create(theonering, Trophy.class);
//
//        Trophy thelight = new Trophy(UUIDgen.getUUID(), "The Light Of Elendil",
//                "I give you the light of Elendil... our most beloved star!",
//                com.questo.android.model.Trophy.Type.FOR_PLACE);
//        manager.create(thelight, Trophy.class);
//
//        Trophy sword = new Trophy(UUIDgen.getUUID(), "The Sword Of Thousand Truths",
//                "We can't trust the Sword of a Thousand Truths to a newb!",
//                com.questo.android.model.Trophy.Type.FOR_QUEST);
//        manager.create(sword, Trophy.class);
//
//        TrophyForUser tfu1 = new TrophyForUser(UUIDgen.getUUID(), mainuser, threeHeadedMonkey.getUuid(), null, null,
//                new Date());
//        manager.create(tfu1, TrophyForUser.class);
//
//        TrophyForUser tfu2 = new TrophyForUser(UUIDgen.getUUID(), mainuser, theonering.getUuid(),
//                stephansdom.getUuid(), null, new Date());
//        manager.create(tfu2, TrophyForUser.class);
//
//        TrophyForUser tfu3 = new TrophyForUser(UUIDgen.getUUID(), mainuser, thelight.getUuid(), peterskirche.getUuid(),
//                null, new Date());
//        manager.create(tfu3, TrophyForUser.class);
//
//        TrophyForUser tfu4 = new TrophyForUser(UUIDgen.getUUID(), mainuser, sword.getUuid(), null, null, new Date());
//        manager.create(tfu4, TrophyForUser.class);
//
//    }
}
