package com.questo.android.common;

public final class Constants {

    public static final String PLACEHOLDER = "{}";
    public static final String NR_QUESTIONS = "app.questo.questions.number";
    public static final String NR_QUESTIONS_LABEL = "This place has <b>" + Constants.PLACEHOLDER + " questions.</b>";
    public static final String NR_ANSWERED_QUESTIONS_LABEL = "You have already answered <b>" + Constants.PLACEHOLDER
            + " questions</b> correctly here.";
    public static final String QUESTION_PROGRESS = "Question {} of {}";
    public static final String QUESTION = "app.questo.questions.question";
    public static final String QUESTION_TYPE = "app.questo.question.type";
    public static final String TRANSITION_OBJECT_UUID = "app.questo.transition.object.uuid";
    public static final String NR_ANSWERED_QUESTIONS = "app.questo.questions.answered";
    public static final String NR_ANSWERED_QUESTIONS_CORRECT = "app.questo.questions.answered.correct";
    public static final String QUEST_SIZE = "app.questo.questions.size";
    public static final String CORRECT_ANSWER = "app.questo.questions.correct";
    public static final String BOOL_CORRECT_ANSWER = "app.questo.questions.correct.bool";
    public static final String CURRENT_TOURNAMENT_TASK_UUID = "app.questo.questions.tournamenttask.uuid";
    public static final String CONGRATS_DETAILS = "You have answered {} of {} questions correctly, earning";
    public static final String EXTRA_COMPANION_UUID_ARRAY = "app.questo.extra.companion.uuid";
    public static final String EXTRA_ADD_QUESTION_PLACE_UUID = "app.questo.extra.add.question.place.uuid";
    
    public static final long MAP_PLACES_NEARBY = 2;
    public static final long MAP_REFRESH_PERIOD = 200;
    public static final String EXTRA_LATITUDE = "app.questo.extra.latitude";
    public static final String EXTRA_LONGITUDE = "app.questo.extra.longitude";

    public static final int QUESTIONS_PER_PLACE = 10;
    public static final String PROFILE_BTN_TYPE = "app.questo.profile.btn";
    public static final String[] PROFILE_BTN_TYPES = {"request", "cancel_request", "cancel_companionship", "accept_companionship"};
    
    private Constants() {

    }
}
