package com.questo.android.model.json;

import java.lang.reflect.Type;

import com.google.gson.InstanceCreator;
import com.questo.android.model.Question;
import com.questo.android.model.Question.PossibleAnswers;

public class PossibleAnswersCreator implements InstanceCreator<PossibleAnswers> {

    @Override
    public PossibleAnswers createInstance(Type type) {
        return new Question().new PossibleAnswers();
    }
    
}