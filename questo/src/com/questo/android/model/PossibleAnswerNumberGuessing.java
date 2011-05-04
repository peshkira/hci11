package com.questo.android.model;

public class PossibleAnswerNumberGuessing implements PossibleAnswer {

    private Integer answer;
    
    public PossibleAnswerNumberGuessing(Integer number) {
        super();
        this.answer = number;
    }
    
    public int getGuessDistance(int guess) {
        return Math.abs(answer - guess);
    }
    
    @Override
    public int compareTo(PossibleAnswer another) {
        return 0;
    }

    public void setNumber(Integer number) {
        this.answer = number;
    }

    public Integer getNumber() {
        return answer;
    }

}
