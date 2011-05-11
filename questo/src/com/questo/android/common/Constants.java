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
    public static final String CONGRATS_DETAILS = "You have answered {} of {} questions correctly, earning";
    
    public static final double MAP_PLACES_NEARBY = 2;
    public static final String EXTRA_LATITUDE = "app.questo.extra.latitude";
    public static final String EXTRA_LONGITUDE = "app.questo.extra.longitude";

    public static final int QUESTIONS_PER_PLACE = 10;
    
    private Constants() {

    }
}
