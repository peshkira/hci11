package com.questo.android.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.questo.android.model.json.JSONizer;

@DatabaseTable
public class Question {
	
	public static final String UUID = "UUID";

	public enum Type {
		MULTIPLE_CHOICE, PLAINTEXT, NUMBERS_GUESSING;
	}

	@DatabaseField(generatedId = true)
	private Integer id;
	@DatabaseField(columnName=UUID)
	private String uuid;
	@DatabaseField
	private Type type;
	@DatabaseField
	private String question;
	@DatabaseField
	private String jsonifiedPossibleAnswers;
	@DatabaseField
	private String jsonifiedCorrectAnswer;
	@DatabaseField(foreign = true)
	private Place place;

	private Question.PossibleAnswers possibleAnswers;
	private Question.CorrectAnswer correctAnswer;

	/**
	 * No-args Constructor only for ORM-access, always use parameterized
	 * constructors.
	 */
	public Question() {
	}

	public Question(String uuid, Type type, String question) {
		super();
		this.uuid = uuid;
		this.type = type;
		this.question = question;
	}

	public String getUuid() {
		return uuid;
	}

	public Integer getId() {
		return id;
	}
	
	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}
	
	public void setPlace(Place place) {
		this.place = place;
	}

	public Place getPlace() {
		return place;
	}
	
	public Question.PossibleAnswers getPossibleAnswers() {
		if (possibleAnswers == null) {
			if (jsonifiedPossibleAnswers == null)
				possibleAnswers = this.new PossibleAnswers();
			else
				possibleAnswers = JSONizer.toObject(jsonifiedPossibleAnswers, Question.PossibleAnswers.class);
		}
		return possibleAnswers;
	}
	
	public Question.CorrectAnswer getCorrectAnswer() {
		if (correctAnswer == null) {
			if (jsonifiedCorrectAnswer == null)
				correctAnswer = this.new CorrectAnswer();
			else
				correctAnswer = JSONizer.toObject(jsonifiedCorrectAnswer, Question.CorrectAnswer.class);
		}
		return correctAnswer;
	}

	@Override
	public boolean equals(Object o) {
		if (!this.getClass().equals(o.getClass()))
			return false;
		Question other = (Question) o;
		return other.id.equals(this.id);
	}

	public class Extras {
		private PossibleAnswer[] possibleAnswers;

		public void setAll(PossibleAnswer[] possibleAnswers) {
			this.possibleAnswers = possibleAnswers;
			afterSetAttribute();
		}

		public PossibleAnswer[] getAll() {
			return possibleAnswers;
		}

		private void afterSetAttribute() {
			Question.this.jsonifiedPossibleAnswers = JSONizer.toJSON(this);
		}
	}
	
	public class PossibleAnswers {
		private PossibleAnswer[] possibleAnswers;

		public void setAll(PossibleAnswer[] possibleAnswers) {
			this.possibleAnswers = possibleAnswers;
			updateJSON();
		}

		public PossibleAnswer[] getAll() {
			return possibleAnswers;
		}

		private void updateJSON() {
			Question.this.jsonifiedPossibleAnswers = JSONizer.toJSON(this);
		}
	}

	public class CorrectAnswer {
		private PossibleAnswer correctAnswer;

		public void set(PossibleAnswer correctAnswer) {
			this.correctAnswer = correctAnswer;
			updateJSON();
		}

		public PossibleAnswer get() {
			return correctAnswer;
		}

		private void updateJSON() {
			Question.this.jsonifiedCorrectAnswer = JSONizer.toJSON(this);
		}
	}

}