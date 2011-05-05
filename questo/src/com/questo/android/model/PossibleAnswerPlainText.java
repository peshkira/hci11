package com.questo.android.model;

public class PossibleAnswerPlainText extends PossibleAnswerImpl {

    public PossibleAnswerPlainText(String a) {
        this.setAnswer(a);
    }
    
    @Override
    public int compareTo(PossibleAnswer another) {
        if (!(another instanceof PossibleAnswerPlainText))
            return 1;
        
        return getAnswer().toLowerCase().compareTo(((PossibleAnswerPlainText) another).getAnswer().toLowerCase());
    }

}
