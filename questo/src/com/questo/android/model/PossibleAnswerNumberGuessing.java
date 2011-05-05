package com.questo.android.model;

public class PossibleAnswerNumberGuessing extends PossibleAnswerImpl {
    
    public PossibleAnswerNumberGuessing(String number) {
        super();
        this.answer = number;
    }
    
    public int getGuessDistance(int guess) {
        return Math.abs(Integer.valueOf(answer) - Integer.valueOf(guess));
    }
    
    @Override
    public int compareTo(PossibleAnswer another) {
        //TODO
        return 0;
    }


}
