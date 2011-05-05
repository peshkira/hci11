package com.questo.android.model.json;

import java.lang.reflect.Type;

import com.google.gson.InstanceCreator;
import com.questo.android.model.Question;
import com.questo.android.model.Question.CorrectAnswer;

public class CorrectAnswerCreator implements InstanceCreator<Question.CorrectAnswer>{

    @Override
    public CorrectAnswer createInstance(Type type) {
        return new Question().new CorrectAnswer();
    }

}
