package com.questo.android.model.test;

import java.util.Date;

import com.questo.android.ModelManager;
import com.questo.android.helper.Security;
import com.questo.android.helper.UUIDgen;
import com.questo.android.model.Companionship;
import com.questo.android.model.Notification;
import com.questo.android.model.Place;
import com.questo.android.model.PossibleAnswer;
import com.questo.android.model.PossibleAnswerImpl;
import com.questo.android.model.PossibleAnswerMultipleChoice;
import com.questo.android.model.PossibleAnswerNumberGuessing;
import com.questo.android.model.PossibleAnswerPlainText;
import com.questo.android.model.Question;
import com.questo.android.model.Question.Type;
import com.questo.android.model.Tournament;
import com.questo.android.model.TournamentMembership;
import com.questo.android.model.TournamentRequest;
import com.questo.android.model.TournamentTask;
import com.questo.android.model.TournamentTaskDone;
import com.questo.android.model.Trophy;
import com.questo.android.model.User;

public class TestDataGenerator {

    private ModelManager manager;

    private User vader, kirk, gandalf, guinevier, bender;

    private Place stephansdom, karlskirche;

    public TestDataGenerator(ModelManager manager) {
        this.manager = manager;
    }

    public void generateTestData() {
        this.generateUsers();
        this.generatePlaces();
        this.generateTournaments();
        this.generateTrophies();
    }

    private void generateUsers() {
        User vader = new User(UUIDgen.getUUID(), new Date());
        vader.setEmail("vader@questo.com");
        vader.setPasswordSalt("lolcat");
        vader.setPasswordHash(Security.md5("questololcat")); // pw = questo
        vader.setName("Darth Vader");
        this.manager.create(vader, User.class);
        this.vader = vader;

        User kirk = new User(UUIDgen.getUUID(), new Date());
        kirk.setEmail("kirk@questo.com");
        kirk.setPasswordSalt("lolcat");
        kirk.setPasswordHash(Security.md5("questololcat"));
        kirk.setName("Captain Kirk");
        manager.create(kirk, User.class);
        this.kirk = kirk;

        User gandalf = new User(UUIDgen.getUUID(), new Date());
        gandalf.setEmail("gandalf@questo.com");
        gandalf.setPasswordSalt("lolcat");
        gandalf.setPasswordHash(Security.md5("questololcat"));
        gandalf.setName("Gandalf");
        manager.create(gandalf, User.class);
        this.gandalf = gandalf;

        User guinevier = new User(UUIDgen.getUUID(), new Date());
        guinevier.setEmail("guinevier@questo.com");
        guinevier.setPasswordSalt("lolcat");
        guinevier.setPasswordHash(Security.md5("questololcat"));
        guinevier.setName("Guinevier");
        manager.create(guinevier, User.class);
        this.guinevier = guinevier;

        User bender = new User(UUIDgen.getUUID(), new Date());
        bender.setEmail("bender@questo.com");
        bender.setPasswordSalt("lolcat");
        bender.setPasswordHash(Security.md5("questololcat"));
        bender.setName("Bender Rodrigez");
        manager.create(bender, User.class);
        this.bender = bender;

        Companionship gand_vader = new Companionship(UUIDgen.getUUID(), gandalf.getUuid(), vader.getUuid(), new Date());
        gand_vader.setConfirmedAt(new Date());
        gand_vader.setConfirmed(true);
        manager.create(gand_vader, Companionship.class);

        Companionship vader_kirk = new Companionship(UUIDgen.getUUID(), vader.getUuid(), kirk.getUuid(), new Date());
        vader_kirk.setConfirmedAt(new Date());
        vader_kirk.setConfirmed(true);
        manager.create(vader_kirk, Companionship.class);

        Companionship bend_vader = new Companionship(UUIDgen.getUUID(), bender.getUuid(), vader.getUuid(), new Date());
        bend_vader.setConfirmed(false);
        manager.create(bend_vader, Companionship.class);

        Notification n = new Notification(UUIDgen.getUUID(),
                com.questo.android.model.Notification.Type.COMPANIONSHIP_REQUEST, "<b>" + bender.getName()
                        + "</b> wants to be your companion", vader, null, new Date());
        manager.create(n, Notification.class);

    }

    private void generatePlaces() {
        Place stephansdom = new Place(UUIDgen.getUUID(), "Stephansdom");
        stephansdom.setLatitude(48.208359);
        stephansdom.setLongitude(16.373298);
        manager.create(stephansdom, Place.class);
        this.stephansdom = stephansdom;

        Place karlskirche = new Place(UUIDgen.getUUID(), "Karlskirche");
        karlskirche.setLatitude(48.19848402746591);
        karlskirche.setLongitude(16.371763944625854);
        this.karlskirche = karlskirche;
        manager.create(karlskirche, Place.class);

        Question q_stephansdom_1 = new Question(UUIDgen.getUUID(), Type.MULTIPLE_CHOICE,
                "How high is the southern tower of the Stephansdom?");
        PossibleAnswerImpl q_stephansdom_1_answer_1 = new PossibleAnswerMultipleChoice(1, "136,4 meters", true);
        PossibleAnswerImpl q_stephansdom_1_answer_2 = new PossibleAnswerMultipleChoice(2, "127,2 meters", false);
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
        
//        Question q_stephansdom_3 = new Question(UUIDgen.getUUID(), Type.NUMBERS_GUESSING, "How high is the shortest tower of Stephansdom");
//        PossibleAnswerImpl qs31 = new PossibleAnswerNumberGuessing("65");
//        q_stephansdom_3.getCorrectAnswer().set(qs31);
//        q_stephansdom_3.setPlace(stephansdom);
//        manager.create(q_stephansdom_3, Question.class);
//        
//        Question q_stephansdom_4 = new Question(UUIDgen.getUUID(), Type.NUMBERS_GUESSING, "How much does the biggest bell of Stephansdom weigh(kg)?");
//        PossibleAnswerImpl qs41 = new PossibleAnswerNumberGuessing("20130");
//        q_stephansdom_4.getCorrectAnswer().set(qs41);
//        q_stephansdom_4.setPlace(stephansdom);
//        manager.create(q_stephansdom_4, Question.class);
//        
//        Question q_stephansdom_5 = new Question(UUIDgen.getUUID(), Type.MULTIPLE_CHOICE, "The capitol of Bulgaria is:");
//        PossibleAnswerImpl qs51 = new PossibleAnswerMultipleChoice(1, "Athens", true);
//        PossibleAnswerImpl qs52 = new PossibleAnswerMultipleChoice(2, "Warsaw", false);
//        PossibleAnswerImpl qs53 = new PossibleAnswerMultipleChoice(2, "Beograd", false);
//        PossibleAnswerImpl qs54 = new PossibleAnswerMultipleChoice(2, "Sarajevo", false);
//        PossibleAnswer[] answers = new PossibleAnswer[] {qs51, qs52, qs53, qs54};
//        q_stephansdom_5.getPossibleAnswers().setAll(answers);
//        q_stephansdom_5.getCorrectAnswer().set(qs51);
//        q_stephansdom_5.setPlace(stephansdom);
//        manager.create(q_stephansdom_5, Question.class);
//        
//        Question q_stephansdom_6 = new Question(UUIDgen.getUUID(), Type.MULTIPLE_CHOICE, "True or False? There is no apostle clock at the cathedral.");
//        PossibleAnswerImpl qs61 = new PossibleAnswerMultipleChoice(1, "true", true);
//        PossibleAnswerImpl qs62 = new PossibleAnswerMultipleChoice(2, "false", false);
//        answers = new PossibleAnswer[] {qs61, qs62};
//        q_stephansdom_6.getPossibleAnswers().setAll(answers);
//        q_stephansdom_6.getCorrectAnswer().set(qs61);
//        q_stephansdom_6.setPlace(stephansdom);
//        manager.create(q_stephansdom_6, Question.class);
        
        Question q_stephansdom_7 = new Question(UUIDgen.getUUID(), Type.PLAINTEXT, "In which Austrian city is Stephansdom located?");
        PossibleAnswerImpl qs71 = new PossibleAnswerPlainText("Vienna");
        q_stephansdom_7.getCorrectAnswer().set(qs71);
        q_stephansdom_7.setPlace(stephansdom);
        manager.create(q_stephansdom_7, Question.class);
        
//        Question q_stephansdom_8 = new Question(UUIDgen.getUUID(), Type.MULTIPLE_CHOICE, "True or False? The cathedral was largely initiated by Rudolf IV.");
//        PossibleAnswerImpl qs81 = new PossibleAnswerMultipleChoice(1, "true", true);
//        PossibleAnswerImpl qs82 = new PossibleAnswerMultipleChoice(2, "false", false);
//        answers = new PossibleAnswer[] {qs81, qs82};
//        q_stephansdom_8.getPossibleAnswers().setAll(answers);
//        q_stephansdom_8.getCorrectAnswer().set(qs81);
//        q_stephansdom_8.setPlace(stephansdom);
//        manager.create(q_stephansdom_8, Question.class);
//        
//        Question q_stephansdom_9 = new Question(UUIDgen.getUUID(), Type.PLAINTEXT, "What is the architectural style of Stephansdom?");
//        PossibleAnswerImpl qs91 = new PossibleAnswerPlainText("Gothic");
//        q_stephansdom_9.getCorrectAnswer().set(qs91);
//        q_stephansdom_9.setPlace(stephansdom);
//        manager.create(q_stephansdom_9, Question.class);  
//
//        Question q_stephansdom_10 = new Question(UUIDgen.getUUID(), Type.NUMBERS_GUESSING, "How long is Stephansdom(meters)?");
//        PossibleAnswerImpl qs101 = new PossibleAnswerNumberGuessing("107");
//        q_stephansdom_10.getCorrectAnswer().set(qs101);
//        q_stephansdom_10.setPlace(stephansdom);
//        manager.create(q_stephansdom_10, Question.class);
        
    }

    private void generateTournaments() {
        Tournament crazyTournament = new Tournament(UUIDgen.getUUID(), new Date(), "Crazy Tournament", "Vienna",
                Tournament.Type.COOP);
        TournamentTask crazyParcourTask1 = new TournamentTask(UUIDgen.getUUID(), crazyTournament, stephansdom.getUuid());
        TournamentTask crazyParcourTask2 = new TournamentTask(UUIDgen.getUUID(), crazyTournament, stephansdom.getUuid());
        TournamentTaskDone crazyParcourTask1Done = new TournamentTaskDone(UUIDgen.getUUID(), vader,
                crazyParcourTask1.getUuid(), new Date());
        TournamentMembership crazyTournamentMembership = new TournamentMembership(UUIDgen.getUUID(), vader.getUuid(),
                crazyTournament.getUuid(), new Date());
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
                kirk.getUuid(), deathmatchTournament.getUuid(), new Date());
        TournamentRequest deathmatchTournamentRequest = new TournamentRequest(UUIDgen.getUUID(), vader.getUuid(),
                kirk.getUuid(), deathmatchTournament.getUuid(), new Date());
        manager.create(deathmatchTournament, Tournament.class);
        manager.create(deathmatchTask1, TournamentTask.class);
        manager.create(deathmatchTournamentMembershipXY, TournamentMembership.class);
        manager.create(deathmatchTournamentRequest, TournamentRequest.class);

    }

    private void generateTrophies() {
        Trophy threeHeadedMonkey = new Trophy(UUIDgen.getUUID(), "Three Headed Monkey", "Look a three-headed monkey!",
                com.questo.android.model.Trophy.Type.GLOBAL);
        manager.create(threeHeadedMonkey, Trophy.class);

        Trophy theonering = new Trophy(UUIDgen.getUUID(), "The One Ring",
                "One Ring to rule them all,<br /> One Ring to find them,<br /> "
                        + "One Ring to bring them all<br /> and in the darkness bind them.<br />",
                com.questo.android.model.Trophy.Type.FOR_PLACE);
        stephansdom.setTrophy(theonering.getUuid());
        stephansdom = manager.update(stephansdom, Place.class);
        manager.create(theonering, Trophy.class);

        Trophy thelight = new Trophy(UUIDgen.getUUID(), "The Light Of Elendil",
                "I give you the light of Elendil... our most beloved star!",
                com.questo.android.model.Trophy.Type.FOR_PLACE);
        karlskirche.setTrophy(thelight.getUuid());
        karlskirche = manager.update(karlskirche, Place.class);
        manager.create(thelight, Trophy.class);

        Trophy sword = new Trophy(UUIDgen.getUUID(), "The Sword Of Thousand Truths",
                "We can't trust the Sword of a Thousand Truths to a newb!", com.questo.android.model.Trophy.Type.GLOBAL);
        manager.create(sword, Trophy.class);
    }
}
