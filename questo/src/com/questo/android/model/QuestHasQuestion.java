package com.questo.android.model;

import java.util.Date;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class QuestHasQuestion {
	@DatabaseField(foreign = true)
	private Quest quest;
	@DatabaseField(foreign = true)
	private Question question;
	@DatabaseField
	private Date answeredAt;
	
	/**
	 * No-args Constructor only for ORM-access, always use parameterized
	 * constructors.
	 */
	public QuestHasQuestion() {
	}
	
	public QuestHasQuestion(Quest quest, Question question, Date answeredAt) {
		super();
		this.quest = quest;
		this.question = question;
		this.answeredAt = answeredAt;
	}

	public Quest getQuest() {
		return quest;
	}

	public Question getQuestion() {
		return question;
	}
	
	public Date getAnsweredAt() {
		return answeredAt;
	}
	
	
}
