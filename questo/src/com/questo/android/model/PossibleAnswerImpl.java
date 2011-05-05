package com.questo.android.model;

public class PossibleAnswerImpl implements PossibleAnswer {

    protected String answer;
    
    public PossibleAnswerImpl() {
        
    }
    
    public PossibleAnswerImpl(String answer) {
        this.setAnswer(answer);
    }
    
    @Override
    public int compareTo(PossibleAnswer another) {
        return 0;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getAnswer() {
        return answer;
    }

}
