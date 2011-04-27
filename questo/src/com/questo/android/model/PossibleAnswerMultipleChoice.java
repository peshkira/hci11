package com.questo.android.model;

public class PossibleAnswerMultipleChoice implements PossibleAnswer {

	private Integer order;
	private String text;
	private boolean correct; // is this possible answer a correct answer to the associated question?
	
	public PossibleAnswerMultipleChoice(Integer order, String text, boolean correct) {
		super();
		this.order = order;
		this.text = text;
		this.correct = correct;
	}

	public Integer getOrder() {
		return order;
	}
	
	public String getText() {
		return text;
	}

	public boolean isCorrect() {
		return correct;
	}

	@Override
	public int compareTo(PossibleAnswer another) {
		if (!(another instanceof PossibleAnswerMultipleChoice))
			return 0;
		PossibleAnswerMultipleChoice other = (PossibleAnswerMultipleChoice)another;
		if (order < other.getOrder())
			return -1;
		else if (order > other.getOrder())
			return 1;
		else
			return 0;
	}

}
