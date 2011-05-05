package com.questo.android.model.json;

import java.lang.reflect.Type;

import com.google.gson.InstanceCreator;
import com.questo.android.model.PossibleAnswer;
import com.questo.android.model.PossibleAnswerImpl;

public class PossibleAnswerCreator implements InstanceCreator<PossibleAnswer> {

    @Override
    public PossibleAnswer createInstance(Type type) {
        return new PossibleAnswerImpl("hack");
    }

}
